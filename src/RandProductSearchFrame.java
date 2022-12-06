import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RandProductSearchFrame extends JFrame {
    JPanel mainPnl, titlePnl, displayPnl, cmdPnl, searchPnl, resultPnl;
    JLabel titleLbl, searchLbl, resultLbl;
    JButton quitBtn, searchBtn;
    JTextField searchTF;
    JTextArea resultTA;
    JScrollPane scroller;
    String search;
    RandomAccessFile raf;

    public RandProductSearchFrame() throws HeadlessException
    {
        setTitle("Product Maker");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int scrnHeight = screenSize.height;
        int scrnWidth = screenSize.width;
        setSize(scrnWidth*3/4, scrnHeight*3/4);
        setLocation(scrnWidth/8, scrnHeight/8);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        add(mainPnl);

        createTitlePanel();
        createDisplayPanel();
        createCommandPanel();

        setVisible(true);
    }
    private void createCommandPanel()
    {
        cmdPnl = new JPanel();
        cmdPnl.setLayout(new GridLayout(1,2));

        quitBtn = new JButton("Quit");
        quitBtn.setFont(new Font("Bold", Font.BOLD, 18));
        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Bold", Font.BOLD, 18));

        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));
        searchBtn.addActionListener((ActionEvent ae) ->
        {
            if (searchTF.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null,"Please enter a search term.","Empty Search",JOptionPane.ERROR_MESSAGE);
            } else {
                search = searchTF.getText();
                try {
                    File file = new File(System.getProperty("user.dir") + "\\src\\productRAF.txt");
                    raf = new RandomAccessFile(file,"rw");
                    for (int i = 0; i < raf.length()/124; i++) {
                        byte[] lookup = new byte[35];
                        if (i==0) {
                            raf.seek(0);
                        } else {
                            raf.seek(i*124);
                        }
                        raf.read(lookup);
                        String name = new String(lookup, StandardCharsets.UTF_8);
                        System.out.println(name);
                        if (name.toLowerCase().contains(search.toLowerCase())) {
                            System.out.println(name.trim());
                            resultLbl.setVisible(true);
                            scroller.setVisible(true);
                            resultTA.append(name.trim() + "\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        cmdPnl.add(searchBtn);
        cmdPnl.add(quitBtn);

        mainPnl.add(cmdPnl, BorderLayout.SOUTH);
    }
    private void createDisplayPanel()
    {
        displayPnl = new JPanel();
        displayPnl.setLayout(new GridLayout(2,1));
        displayPnl.setBorder(new EmptyBorder(0,100,0,100));

        searchPnl = new JPanel();
        searchLbl = new JLabel("Enter your search term:");
        searchTF = new JTextField(20);

        resultPnl = new JPanel();
        resultPnl.setLayout(new BoxLayout(resultPnl,BoxLayout.Y_AXIS));
        resultLbl = new JLabel("Products containing your search term:");
        resultLbl.setVisible(false);
        resultTA = new JTextArea();
        resultTA.setBorder(new EmptyBorder(4,4,4,4));
        resultTA.setEditable(false);
        scroller = new JScrollPane(resultTA);
        scroller.setVisible(false);

        searchPnl.add(searchLbl);
        searchPnl.add(searchTF);
        resultPnl.add(resultLbl);
        resultPnl.add(scroller);

        displayPnl.add(searchPnl);
        displayPnl.add(resultPnl);

        mainPnl.add(displayPnl, BorderLayout.CENTER);
    }
    private void createTitlePanel()
    {
        titlePnl = new JPanel();

        titleLbl = new JLabel("Product Search", JLabel.CENTER);
        titleLbl.setVerticalTextPosition(JLabel.BOTTOM);
        titleLbl.setHorizontalTextPosition(JLabel.CENTER);
        titleLbl.setFont(new Font("Bold Italic", Font.BOLD | Font.ITALIC, 36));

        titlePnl.add(titleLbl);

        mainPnl.add(titlePnl, BorderLayout.NORTH);
    }
}
