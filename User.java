public class User {
    private String id;
    private String password;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public static boolean authenticate(String id, String password) {
        return id.equals("dosen123") && password.equals("password123");
    }
}
