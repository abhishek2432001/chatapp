import java.net.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DocumentFilter.FilterBypass;

import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
public class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br; //For reading
    PrintWriter out; //For writing

     //Declare Components
     private JLabel heading=new JLabel("Server Area");
     private JTextArea messageArea=new JTextArea();
     private JTextField messageInput = new JTextField();
     private Font font=new Font("SERIF",Font.BOLD,20);

//constructor
    public Server(){
        try{
            server = new ServerSocket(7780);
            System.out.println("Server is ready to accept connecn");
            System.out.println("Waiting");
            socket = server.accept();

            br  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out  = new  PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startReading();
            // startWriting();
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Key Released "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    // System.out.println("You pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                // throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
            }
            
        });
    }
    private void createGUI(){
        //gui code

        this.setTitle("Server Window");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        //For componets
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("logo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //layout of frame
        this.setLayout(new BorderLayout());

        //Adding components to frame

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jscrollPane=new JScrollPane(messageArea);
        this.add(jscrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


    }
    public static void main(String[] args){
        System.out.println("Going to start server");
        new Server();
    }

    public void startReading(){
        //thread read krke dega
        Runnable r1=()->{
            System.out.println("Reader started");
            try{
            while(true){
                
                    String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Client terminated chat");
                    break;
                }
                System.out.println("Client : "+msg);
                
                
            }
        } catch (Exception e){
                // e.printStackTrace();
                System.out.println("Connecn is closed");
            }
        };
        new Thread(r1).start();

    }
    public void startWriting(){
        //thread- data user lega aur send krega client tk
        Runnable r2=()->{
            try {
            while(true && !socket.isClosed()){
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("exit")){
                        socket.close();
                        break;
                    }
            }
            System.out.println("Connecn is closed");
        }catch (Exception e){
            e.printStackTrace();
        }
    };
        new Thread(r2).start();

    }
}