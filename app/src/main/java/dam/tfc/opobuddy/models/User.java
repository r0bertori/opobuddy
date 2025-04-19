package dam.tfc.opobuddy.models;

public class User {

    private String username;
    private String correo;
    private String pwd;

    public User() {
    }

    public User(String username, String correo, String pwd) {
        this.username = username;
        this.correo = correo;
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
