package server.communication;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import server.service.FacultyHandler;
import server.service.StudentHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.UUID;

public class CommunicationController {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8080;
    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final int STATUS_OK = 200;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    //Handlers
    private static FacultyHandler facultyHandler = new FacultyHandler();
    private static StudentHandler studentHandler = new StudentHandler();

    public static void start() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), 1);

        //-----------------------------------------Handle HTML\JS Requests------------------------------------------
        server.createContext("/", he -> {
            String pathToRoot = new File("count-me-in\\src\\client\\").getAbsolutePath().concat("\\");
            String path = he.getRequestURI().getPath();
            try {
                path = path.substring(1);
                path = path.replaceAll("//", "/");
                if (path.length() == 0)
                    path = "html\\Main.html";

                boolean fromFile = new File(pathToRoot + path).exists();
                InputStream in = fromFile ? new FileInputStream(pathToRoot + path)
                        : ClassLoader.getSystemClassLoader().getResourceAsStream(pathToRoot + path);

                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                OutputStream gout = new DataOutputStream(bout);
                byte[] tmp = new byte[4096];
                int r;
                while ((r = in.read(tmp)) >= 0)
                    gout.write(tmp, 0, r);
                gout.flush();
                gout.close();
                in.close();
                byte[] data = bout.toByteArray();

                if (path.endsWith(".js"))
                    he.getResponseHeaders().set("Content-Type", "text/javascript");
                else if (path.endsWith(".html"))
                    he.getResponseHeaders().set("Content-Type", "text/html");
                else if (path.endsWith(".css"))
                    he.getResponseHeaders().set("Content-Type", "text/css");
                else if (path.endsWith(".json"))
                    he.getResponseHeaders().set("Content-Type", "application/json");
                else if (path.endsWith(".svg"))
                    he.getResponseHeaders().set("Content-Type", "image/svg+xml");
                if (he.getRequestMethod().equals("HEAD")) {
                    he.getResponseHeaders().set("Content-Length", "" + data.length);
                    he.sendResponseHeaders(200, -1);
                    return;
                }

                he.sendResponseHeaders(200, data.length);
                he.getResponseBody().write(data);
                he.getResponseBody().close();
            } catch (NullPointerException t) {
                System.err.println("Error retrieving: " + path);
            } catch (Throwable t) {
                System.err.println("Error retrieving: " + path);
                t.printStackTrace();
            }

        });


        //-----------------------------------------FacultyHandler cases------------------------------------------
        //accept: {session_id:String, email:String, password:String}
        //retrieve: {SUCCESS: msg} OR ERROR //???????
        server.createContext("/count-me-in/loginFaculty", he -> {
            final Headers headers = he.getResponseHeaders();

            try {
                //final String requestMethod = he.getRequestMethod();
                byte[] requestByte = he.getRequestBody().readAllBytes();
                JSONParser parser = new JSONParser();
                JSONObject requestJson = (JSONObject) parser.parse(new String(requestByte));
                String email = (requestJson.containsKey("email")) ? (String) requestJson.get("email") : null;
                String password = (requestJson.containsKey("password")) ? (String) (requestJson.get("password")) : null;
                String session_id = (requestJson.containsKey("session_id")) ?  (requestJson.get("session_id").toString()) : "";
                String response = facultyHandler.login(UUID.fromString(session_id),email, password);
                headers.set("loginFaculty", String.format("application/json; charset=%s", UTF8));
                sendResponse(he, response);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                headers.set("loginFaculty", String.format("application/json; charset=%s", UTF8));
                sendERROR(he, e.getMessage());
            } finally {
                he.close();
            }
        });

        //accept: {slotID:int}
        //retrieve: ???????
        server.createContext("/count-me-in/getRegisteredStudents", he -> {
            final Headers headers = he.getResponseHeaders();

            try {
                //final String requestMethod = he.getRequestMethod();
                byte[] requestByte = he.getRequestBody().readAllBytes();
                JSONParser parser = new JSONParser();
                JSONObject requestJson = (JSONObject) parser.parse(new String(requestByte));
                int slotID = (requestJson.containsKey("slotOD")) ? Integer.parseInt(requestJson.get("slotID").toString()) : null;
                String response = facultyHandler.getRegisteredStudents(slotID);
                headers.set("getRegisteredStudents", String.format("application/json; charset=%s", UTF8));
                sendResponse(he, response);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                headers.set("getRegisteredStudents", String.format("application/json; charset=%s", UTF8));
                sendERROR(he, e.getMessage());
            } finally {
                he.close();
            }
        });

        //accept: {courseID:String, groupID:int}
        //retrieve: ???????
        server.createContext("/count-me-in/getSlots", he -> {
            final Headers headers = he.getResponseHeaders();

            try {
                //final String requestMethod = he.getRequestMethod();
                byte[] requestByte = he.getRequestBody().readAllBytes();
                JSONParser parser = new JSONParser();
                JSONObject requestJson = (JSONObject) parser.parse(new String(requestByte));
                String courseID = (requestJson.containsKey("courseID")) ? (String) requestJson.get("courseID") : null;
                int groupID = (requestJson.containsKey("groupID")) ? Integer.parseInt(requestJson.get("groupID").toString()) : null;
                String response = facultyHandler.getSlots(courseID, groupID);
                headers.set("getSlots", String.format("application/json; charset=%s", UTF8));
                sendResponse(he, response);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                headers.set("getSlots", String.format("application/json; charset=%s", UTF8));
                sendERROR(he, e.getMessage());
            } finally {
                he.close();
            }
        });


        //-----------------------------------------StudentHandler cases------------------------------------------
        //accept: {session_id:String, email:String, password:String}
        //retrieve: ???????
        server.createContext("/count-me-in/loginStudent", he -> {
            final Headers headers = he.getResponseHeaders();

            try {
                //final String requestMethod = he.getRequestMethod();
                byte[] requestByte = he.getRequestBody().readAllBytes();
                JSONParser parser = new JSONParser();
                JSONObject requestJson = (JSONObject) parser.parse(new String(requestByte));
                String email = (requestJson.containsKey("email")) ? (String) requestJson.get("email") : null;
                String password = (requestJson.containsKey("password")) ? (String) (requestJson.get("password")) : null;
                String session_id = (requestJson.containsKey("session_id")) ?  (requestJson.get("session_id").toString()) : "";
                String response = studentHandler.login(UUID.fromString(session_id),email, password);
                headers.set("loginStudent", String.format("application/json; charset=%s", UTF8));
                sendResponse(he, response);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                headers.set("loginStudent", String.format("application/json; charset=%s", UTF8));
                sendERROR(he, e.getMessage());
            } finally {
                he.close();
            }
        });

        //accept: {session_id:String}
        //retrieve: ???????
        server.createContext("/count-me-in/getStudentPoints", he -> {
            final Headers headers = he.getResponseHeaders();

            try {
                //final String requestMethod = he.getRequestMethod();
                byte[] requestByte = he.getRequestBody().readAllBytes();
                JSONParser parser = new JSONParser();
                JSONObject requestJson = (JSONObject) parser.parse(new String(requestByte));
                String session_id = (requestJson.containsKey("session_id")) ?  (requestJson.get("session_id").toString()) : "";
                String response = studentHandler.getStudentPoints(UUID.fromString(session_id));
                headers.set("getStudentPoints", String.format("application/json; charset=%s", UTF8));
                sendResponse(he, response);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                headers.set("getStudentPoints", String.format("application/json; charset=%s", UTF8));
                sendERROR(he, e.getMessage());
            } finally {
                he.close();
            }
        });


        //accept: {session_id:String, slotID:int, percentage:int}
        //retrieve: ???????
        server.createContext("/count-me-in/updatePercentage", he -> {
            final Headers headers = he.getResponseHeaders();

            try {
                //final String requestMethod = he.getRequestMethod();
                byte[] requestByte = he.getRequestBody().readAllBytes();
                JSONParser parser = new JSONParser();
                JSONObject requestJson = (JSONObject) parser.parse(new String(requestByte));
                int slotID = (requestJson.containsKey("slotID")) ? Integer.parseInt(requestJson.get("slotID").toString()) : null;
                int percentage = (requestJson.containsKey("percentage")) ? Integer.parseInt(requestJson.get("percentage").toString()) : null;
                String session_id = (requestJson.containsKey("session_id")) ?  (requestJson.get("session_id").toString()) : "";
                studentHandler.updatePercentage(UUID.fromString(session_id), slotID, percentage);
                headers.set("updatePercentage", String.format("application/json; charset=%s", UTF8));
                //sendResponse(he, response);TODO: ???
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                headers.set("updatePercentage", String.format("application/json; charset=%s", UTF8));
                sendERROR(he, e.getMessage());
            } finally {
                he.close();
            }
        });

        //accept: {session_id:String}
        //retrieve: ???????
        server.createContext("/count-me-in/getSchedule", he -> {
            final Headers headers = he.getResponseHeaders();

            try {
                //final String requestMethod = he.getRequestMethod();
                byte[] requestByte = he.getRequestBody().readAllBytes();
                JSONParser parser = new JSONParser();
                JSONObject requestJson = (JSONObject) parser.parse(new String(requestByte));
                String session_id = (requestJson.containsKey("session_id")) ?  (requestJson.get("session_id").toString()) : "";
                String response = studentHandler.getSchedule(UUID.fromString(session_id));
                headers.set("getSchedule", String.format("application/json; charset=%s", UTF8));
                sendResponse(he, response);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                headers.set("getSchedule", String.format("application/json; charset=%s", UTF8));
                sendERROR(he, e.getMessage());
            } finally {
                he.close();
            }
        });

        //accept: {session_id:String}
        //retrieve: ???????
        server.createContext("/count-me-in/getScheduleBiding", he -> {
            final Headers headers = he.getResponseHeaders();

            try {
                //final String requestMethod = he.getRequestMethod();
                byte[] requestByte = he.getRequestBody().readAllBytes();
                JSONParser parser = new JSONParser();
                JSONObject requestJson = (JSONObject) parser.parse(new String(requestByte));
                String session_id = (requestJson.containsKey("session_id")) ?  (requestJson.get("session_id").toString()) : "";
                String response = studentHandler.getScheduleBiding(UUID.fromString(session_id));
                headers.set("getScheduleBiding", String.format("application/json; charset=%s", UTF8));
                sendResponse(he, response);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                headers.set("getScheduleBiding", String.format("application/json; charset=%s", UTF8));
                sendERROR(he, e.getMessage());
            } finally {
                he.close();
            }
        });
    }

    public static void sendResponse(HttpExchange he, String response) throws IOException {
        final byte[] ResponseBytes = response.getBytes(UTF8);
        he.sendResponseHeaders(STATUS_OK, ResponseBytes.length);
        he.getResponseBody().write(ResponseBytes);
    }

    public static void sendERROR(HttpExchange he, String response) throws IOException {
        final byte[] ResponseBytes = response.getBytes(UTF8);
        he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, ResponseBytes.length);
        he.getResponseBody().write(ResponseBytes);
    }



}
