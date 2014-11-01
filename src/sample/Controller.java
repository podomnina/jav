//Надо пофиксить: если перемещаем вершину, то нужно изменить ее координаты в списке vertexList//СДЕЛАНО
//После того, как нажимаем Delete, нельзя добавлять вершины. Надо исправить
//Сделала несколько кружков, удалила, нажала New, потому что иначе не добавляются новые. Почему-то все можно перемещать, а первый нет.//ПОФИКШЕНО

package sample;

import com.sun.deploy.util.BlackList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Button;
import java.awt.Color;
import java.awt.image.ImageProducer;
import java.util.ArrayList;

public class Controller extends Application {
    //----------------------Model---------------------//
    public static ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
    public static ArrayList<Line> lineList = new ArrayList<Line>();
    public static Vertex vertex;
    public static int i=0;   //счетчик вершин
//----------------------ModelEND---------------------//



    public double getSampleWidth() { return 500; }

    public double getSampleHeight() { return 500; }

    private Group root;

    private javafx.scene.control.Button NewFile;
    private javafx.scene.control.Button OpenFile;
    private javafx.scene.control.Button DeleteObject;
    private javafx.scene.control.Button Empty;
    private javafx.scene.control.Button Refresh;


    private ImageView background;
    private ImageView image;

    private double initX;
    private double initY;
    private Point2D dragAnchor;
    private ToolBar toolbar;





    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Graph Editor");
        root = new Group();

        Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        scene.getStylesheets().add
                (Main.class.getResource("/sample/Style.css").toExternalForm());
        primaryStage.show();



        background = new ImageView(getClass().getResource("11.jpg").toExternalForm());
        image = new ImageView(getClass().getResource("11.jpg").toExternalForm());
        root.getChildren().addAll(background,image);




//-----------------------------TOOLBAR-------------------------------------------//
        toolbar = new ToolBar();
        toolbar.getItems().add(NewFile = new javafx.scene.control.Button("New"));
        toolbar.getItems().add(OpenFile = new javafx.scene.control.Button("Open"));
        toolbar.getItems().add(Empty = new javafx.scene.control.Button());
        toolbar.getItems().add(DeleteObject = new javafx.scene.control.Button("Delete"));
        toolbar.getItems().add(Refresh = new javafx.scene.control.Button("Refresh"));


        root.getChildren().add(toolbar);

        toolbar.setId("toolbar");

        toolbar.setMinSize(950,40);

        DeleteObject.setId("DeleteObject");
        NewFile.setId("NewFile");
        OpenFile.setId("OpenFile");
        Empty.setId("Empty");
        Refresh.setId("Refresh");

        root.setId("All");


        DeleteObject.setMinSize(70, 30);

        NewFile.setMinSize(70, 30);

        OpenFile.setMinSize(70, 30);

        Refresh.setMinSize(70,30);

        Empty.setMinSize(100, 30);
        Empty.setVisible(false);

//-----------------------------TOOLBAR_end-------------------------------------------//

        //NewFile.setOnAction(new EventHandler<ActionEvent>() {
        //  public void handle(ActionEvent event) {

        image.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //when mouse is pressed, store initial position

                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
                final Circle circleBig = createCircle("Circle", javafx.scene.paint.Color.BLUE, 30);

                circleBig.setTranslateX(me.getSceneX());
                circleBig.setTranslateY(me.getSceneY());

                System.out.println(circleBig.getTranslateX());
                System.out.println(circleBig.getTranslateY());

                vertex = new Vertex(++i,circleBig.getTranslateX(),circleBig.getTranslateY(),30);
                vertexList.add(vertex);
                System.out.println(vertex.getNum()+ " " + vertex.getX()+ " " + vertex.getY());
                root.getChildren().add(circleBig);
                circleBig.setId("Circle");
            }
        });

        //   }
        // });

        DeleteObject.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                vertexList.remove(vertexList.size()-1);
                Refresh();
            }

        });

        Refresh.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Refresh();
            }
        });

    }

    private void Refresh(){
        image.toFront();
        toolbar.toFront();

        for (int j=0;j<vertexList.size();j++)
        {
            final Circle circle = createCircle("Circle", javafx.scene.paint.Color.RED, vertexList.get(j).getRadius());
            circle.setTranslateX(vertexList.get(j).getX());
            circle.setTranslateY(vertexList.get(j).getY());
            root.getChildren().add(circle);
            System.out.println("Success!");
        }

    }

    private Circle createCircle(final String name, final javafx.scene.paint.Color color, int radius) {
        //create a circle with desired name,  color and radius
        final Circle circle = new Circle(radius, new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[] {
                new Stop(0, javafx.scene.paint.Color.rgb(250, 250, 255)),
                new Stop(1, color)
        }));
        //add a shadow effect
        circle.setEffect(new InnerShadow(7, color.darker().darker()));
        //change a cursor when it is over circle
        circle.setCursor(Cursor.HAND);
        //add a mouse listeners
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                // showOnConsole("Clicked on" + name + ", " + me.getClickCount() + "times");
                //the event will be passed only to the circle which is on front
                me.consume();
            }
        });
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                double dragX = me.getSceneX() - dragAnchor.getX();
                double dragY = me.getSceneY() - dragAnchor.getY();
                //calculate new position of the circle
                double newXPosition = initX + dragX;
                double newYPosition = initY + dragY;

                double FirstX = circle.getTranslateX();
                double FirstY = circle.getTranslateY();
                int NumberOfCircle=0;
                //if new position do not exceeds borders of the rectangle, translate to this position
                for (int j=0;j<vertexList.size();j++)
                {
                    if ((FirstX==vertexList.get(j).getX())&&(FirstY==vertexList.get(j).getY()))
                        NumberOfCircle = j;
                }

                if ((newXPosition>=circle.getRadius()) && (newXPosition<=750-circle.getRadius())) {
                    circle.setTranslateX(newXPosition);
                    vertexList.get(NumberOfCircle).setX(newXPosition);
                }
                if ((newYPosition>=50+circle.getRadius())&&(newYPosition>=circle.getRadius()) && (newYPosition<=450-circle.getRadius())){
                    circle.setTranslateY(newYPosition);
                    vertexList.get(NumberOfCircle).setY(newYPosition);
                }
                // showOnConsole(name + " was dragged (x:" + dragX + ", y:" + dragY +")");
            }
        });
        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //change the z-coordinate of the circle
                circle.toFront();
                //showOnConsole("Mouse entered " + name);
            }
        });
        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //showOnConsole("Mouse exited " +name);
            }
        });
        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //when mouse is pressed, store initial position
                initX = circle.getTranslateX();
                initY = circle.getTranslateY();
                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
                //showOnConsole("Mouse pressed above " + name);
            }
        });
        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //showOnConsole("Mouse released above " +name);
            }
        });


        return circle;
    }


    public static void main(String[] args) { launch(args); }
}
