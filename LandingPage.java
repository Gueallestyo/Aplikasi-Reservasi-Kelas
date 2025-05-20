import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {
    public LandingPage(String dosenId) {
        setTitle("Dashboard Dosen");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Selamat datang, " + dosenId + "!", SwingConstants.CENTER);
        JLabel infoLabel = new JLabel("Ini adalah halaman utama reservasi ruang kelas.", SwingConstants.CENTER);

        setLayout(new GridLayout(2, 1));
        add(welcomeLabel);
        add(infoLabel);

        setVisible(true);
    }
}