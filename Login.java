import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Login extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private ArrayList<User> users;

    public Login() {
        setTitle("Login Dosen");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        users = new ArrayList<>();
        users.add(new User("dosen01", "password123"));
        users.add(new User("dosen02", "admin456"));

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String password = new String(passwordField.getPassword());

                if (checkLogin(id, password)) {
                    JOptionPane.showMessageDialog(null, "Login berhasil!");
                    dispose();
                    new LandingPage(id);
                } else {
                    JOptionPane.showMessageDialog(null, "Login gagal! Coba lagi.");
                }
            }
        });

        setVisible(true);
    }

    private boolean checkLogin(String id, String password) {
        for (User user : users) {
            if (user.getId().equals(id) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
