import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameBoard extends JPanel implements ActionListener {

    int width = 400;
    int height = 400;

    //position of the dot
    int x [] = new int [height * width];
    int y [] = new int [height * width];

    int dots = 3;
    int Dot_size = 10;

    int apple_x = 100;
    int apple_y = 100;

    Image apple;
    Image head;
    Image body;

    int point = 0;

    Image game_over;

    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;

    Timer timer;
    int delay =150;
    int RAND_POS = 39;
    boolean inGame = true;

    public GameBoard(){
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(width ,height));
        setBackground(Color.darkGray);
        loadImages();
        initGame();
        move();
    }

    public void initGame(){
        dots = 3;
        for(int i = 0 ; i < dots ; i++){

            x[i] = 150 + i * Dot_size ;
            y[i] = 150;
        }
        timer = new Timer(delay , this);
        timer.start();
    }
    private void loadImages(){
        ImageIcon image_apple = new ImageIcon("src/resources/apple.png");
        apple = image_apple.getImage();

        ImageIcon image_body = new ImageIcon("src/resources/dot.png");
        body = image_body.getImage();

        ImageIcon image_head = new ImageIcon("src/resources/head.png");
        head = image_head.getImage();

        ImageIcon image_game_over = new ImageIcon("src/resources/game.png");
        game_over = image_game_over.getImage();

    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        if(inGame){

            graphics.drawImage(apple , apple_x , apple_y , this);
            Score(graphics);

            for(int i = 0 ; i < dots; i++){
                if(i == 0){
                    graphics.drawImage(head , x[0] , y[0] , this);
                }else{
                    graphics.drawImage(body , x[i] , y[i] , this);
                }
            }
            //image Sync with new postion of the snake
            Toolkit.getDefaultToolkit().sync();
        }else {
            gameOver(graphics);
        }
    }

    private void move(){

        for(int i = dots - 1; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if(leftDirection){
            x[0] -= Dot_size;
        }
        if(rightDirection){
            x[0] += Dot_size;
        }
        if(upDirection){
            y[0] -= Dot_size;
        }
        if(downDirection){
            y[0] += Dot_size;
        }
    }

    private void locateApple(){
        int r = (int)(Math.random()*(RAND_POS));//math.random give double value it should be
                                                // typecased
        apple_x = r*Dot_size;
        r = (int)(Math.random()*(RAND_POS));
        apple_y = r*Dot_size;
    }
    private void checkApple(){
        if(x[0] == apple_x && y[0] == apple_y){
            dots++;
            locateApple();
        }
    }

    private void checkCollision(){
        if(x[0] < 0){
            inGame = false;
        }
        if(x[0] >= width){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
        if(y[0] >= height){
            inGame = false;
        }

        for(int i = dots-1; i >= 3; i--){
            if(x[0] == x[i] && y[0] == y[i]){
                inGame = false;
                break;
            }
        }
    }
    private void gameOver(Graphics graphics){

        graphics.drawImage(game_over , (width/2 - 75) , height/3 , this);
//        String msg = "Game Over";
//        Font small = new Font("Helvetica" , Font.BOLD , 14);
//        FontMetrics metrics = getFontMetrics(small);
//        graphics.setColor(Color.WHITE);
//        graphics.setFont(small);
//
//        graphics.drawString(msg , (width-metrics.stringWidth(msg))/2 , height/2);
    }
    private void Score(Graphics graphics){
        String points = "Score : point ";
        Font small = new Font("Helvetica" , Font.BOLD , 14);
        FontMetrics metrics = getFontMetrics(small);
        graphics.setColor(Color.WHITE);
        graphics.setFont(small);
        graphics.drawString(points , width-150, height-375);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();

            if(key == KeyEvent.VK_LEFT && (!rightDirection)){

                leftDirection = true;
                downDirection= false;
                upDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && (!leftDirection)){

                rightDirection = true;
                downDirection= false;
                upDirection = false;
            }
            if(key == KeyEvent.VK_UP && (!downDirection)){

                upDirection = true;
                rightDirection= false;
                leftDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && (!upDirection)){

                leftDirection = false;
                downDirection= true;
                rightDirection = false;
            }
        }

    }

}

