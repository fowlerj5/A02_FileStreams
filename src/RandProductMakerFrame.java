import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class RandProductMakerFrame extends JFrame {
    JPanel mainPnl, titlePnl, displayPnl, cmdPnl;
    JLabel titleLbl, nameLbl, descriptionLbl, IDLbl, costLbl, countLbl;
    JTextField nameTF, descriptionTF, IDTF, costTF, countTF;
    JButton quitBtn, addBtn;
    String name, description, ID;
    double cost;
    int entries = 0;
    RandomAccessFile raf;

    public RandProductMakerFrame() throws HeadlessException
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
        addBtn = new JButton("Add Product");
        addBtn.setFont(new Font("Bold", Font.BOLD, 18));

        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));
        addBtn.addActionListener((ActionEvent ae) ->
        {
            try {
                File file = new File(System.getProperty("user.dir") + "\\src\\productRAF.txt");
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                }
                raf = new RandomAccessFile(file,"rw");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (
                !nameTF.getText().isEmpty() && nameTF.getText().length()<=35 &&
                !descriptionTF.getText().isEmpty() && descriptionTF.getText().length()<=35 &&
                !IDTF.getText().isEmpty() && IDTF.getText().length()==6 &&
                !costTF.getText().isEmpty()
            ) {
                name = nameTF.getText();
                description = descriptionTF.getText();
                ID = IDTF.getText();
                cost = Double.valueOf(costTF.getText());
                int nameLen = name.length();
                int descLen = description.length();
                for (int i = 0; i < 35 - nameLen; i++) {
                    name = name + " ";
                }
                for (int i = 0; i < 75 - descLen; i++) {
                    description = description + " ";
                }
                byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
                byte[] descBytes = description.getBytes(StandardCharsets.UTF_8);
                byte[] IDBytes = ID.getBytes(StandardCharsets.UTF_8);
                try {
                    if (entries==0) {
                        raf.seek(0);
                    } else if (entries>=1) {
                        raf.seek(entries*124);
                    }
                    raf.write(nameBytes);
                    raf.write(descBytes);
                    raf.write(IDBytes);
                    raf.writeDouble(cost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nameTF.setText("");
                descriptionTF.setText("");
                IDTF.setText("");
                costTF.setText("");
                entries++;
                countTF.setText(String.valueOf(entries));
            } else if (nameTF.getText().isEmpty()||descriptionTF.getText().isEmpty()||IDTF.getText().isEmpty()||costTF.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null,"Please enter values in all fields.","Empty Field(s)",JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,"Please ensure all values are of the correct length.\nName must be at most 35 characters.\nDescription must be at most 75 characters.\nID must be exactly 6 characters.","Field Length(s)",JOptionPane.ERROR_MESSAGE);
            }
        });

        cmdPnl.add(addBtn);
        cmdPnl.add(quitBtn);

        mainPnl.add(cmdPnl, BorderLayout.SOUTH);
    }
    private void createDisplayPanel()
    {
        displayPnl = new JPanel();
        displayPnl.setLayout(new GridLayout(5,2));

        nameLbl = new JLabel("Name:");
        nameTF = new JTextField(100);

        descriptionLbl = new JLabel("Description:");
        descriptionTF = new JTextField(100);

        IDLbl = new JLabel("ID:");
        IDTF = new JTextField(100);

        costLbl = new JLabel("Price:");
        costTF = new JTextField(100);

        countLbl = new JLabel("Product Records Entered:");
        countTF = new JTextField(String.valueOf(entries));
        countTF.setEditable(false);

        displayPnl.add(nameLbl);
        displayPnl.add(nameTF);
        displayPnl.add(descriptionLbl);
        displayPnl.add(descriptionTF);
        displayPnl.add(IDLbl);
        displayPnl.add(IDTF);
        displayPnl.add(costLbl);
        displayPnl.add(costTF);
        displayPnl.add(countLbl);
        displayPnl.add(countTF);

        mainPnl.add(displayPnl, BorderLayout.CENTER);
    }
    private void createTitlePanel()
    {
        titlePnl = new JPanel();

        titleLbl = new JLabel("Product Maker", JLabel.CENTER);
        titleLbl.setVerticalTextPosition(JLabel.BOTTOM);
        titleLbl.setHorizontalTextPosition(JLabel.CENTER);
        titleLbl.setFont(new Font("Bold Italic", Font.BOLD | Font.ITALIC, 36));

        titlePnl.add(titleLbl);

        mainPnl.add(titlePnl, BorderLayout.NORTH);
    }
}
