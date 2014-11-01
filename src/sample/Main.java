//Надо пофиксить: если перемещаем вершину, то нужно изменить ее координаты в списке vertexList//СДЕЛАНО
//После того, как нажимаем Delete, нельзя добавлять вершины. Надо исправить
//Сделала несколько кружков, удалила, нажала New, потому что иначе не добавляются новые. Почему-то все можно перемещать, а первый нет.//ПОФИКШЕНО
//Доделать Refresh. Исправила с красного на синий, ибо после нажатия рефреша и продолжения работы цвет остается красный.
//Доделать удаление любого кружка
//По окончании почистить код и Sample
//Изменила перемещение кружков(теперь они не двигаются). Их перемещения мешает рисолвать стрелки
package sample;



import javafx.scene.shape.Line;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import java.util.ArrayList;



public class Main extends Application {
    //----------------------Model---------------------//
    public static ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
    public static ArrayList<ILine> lineList = new ArrayList<ILine>();
    public static Vertex vertex;
    public static int i = 0;   //счетчик вершин
//----------------------ModelEND---------------------//


    public double getSampleWidth() {
        return 500;
    }

    public double getSampleHeight() {
        return 500;
    }

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
        root.getChildren().addAll(background, image);


//-----------------------------TOOLBAR-------------------------------------------//
        toolbar = new ToolBar();
        toolbar.getItems().add(NewFile = new javafx.scene.control.Button("New"));
        toolbar.getItems().add(OpenFile = new javafx.scene.control.Button("Open"));
        toolbar.getItems().add(Empty = new javafx.scene.control.Button());
        toolbar.getItems().add(DeleteObject = new javafx.scene.control.Button("Delete"));
        toolbar.getItems().add(Refresh = new javafx.scene.control.Button("Refresh"));


        root.getChildren().add(toolbar);

        toolbar.setId("toolbar");

        toolbar.setMinSize(950, 40);

        DeleteObject.setId("DeleteObject");
        NewFile.setId("NewFile");
        OpenFile.setId("OpenFile");
        Empty.setId("Empty");
        Refresh.setId("Refresh");

        root.setId("All");


        DeleteObject.setMinSize(70, 30);

        NewFile.setMinSize(70, 30);

        OpenFile.setMinSize(70, 30);

        Refresh.setMinSize(70, 30);

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

                vertex = new Vertex(++i, circleBig.getTranslateX(), circleBig.getTranslateY(), 30);
                vertexList.add(vertex);
                System.out.println(vertex.getNum() + " " + vertex.getX() + " " + vertex.getY());
                root.getChildren().add(circleBig);
                circleBig.setId("Circle");
            }
        });

        //   }
        // });

        DeleteObject.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {


                vertexList.remove(vertexList.size() - 1);

                Refresh();
            }

        });

        Refresh.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Refresh();
            }
        });

        OpenFile.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                for (Vertex vertex : vertexList) {
                    System.out.println("Вершина №" + vertex.getNum() + "с координатами:" + vertex.getX() + ";" + vertex.getY());
                }
                for (ILine line : lineList) {
                    System.out.println("Ребро начальная вершина:" + line.getStartVertex() + " конечная:" + line.getEndVertex());
                }
            }
        });

    }

    private void Refresh() {
        image.toFront();
        toolbar.toFront();

        for (int j=0; j < lineList.size();j++) {
            final Line line = new Line();
            final Line line1 = new Line();
            final Line line2 = new Line();
            line.setStartX(lineList.get(j).getStartVertex().getX());
            line.setStartY(lineList.get(j).getStartVertex().getY());
            line.setEndX(lineList.get(j).getEndVertex().getX());
            line.setEndY(lineList.get(j).getEndVertex().getY());
            line.setFill(null);
            line.setStroke(javafx.scene.paint.Color.RED);
            line.setStrokeWidth(2);
            //CreateArrowForRefresh(line,line1,line2);

            Double k = Math.atan((lineList.get(j).getEndVertex().getY() - lineList.get(j).getStartVertex().getY()) / (lineList.get(j).getEndVertex().getX() - lineList.get(j).getStartVertex().getX()));
            if (lineList.get(j).getStartVertex().getX() > lineList.get(j).getEndVertex().getX()) {
                line.setEndX(lineList.get(j).getEndVertex().getX() + lineList.get(j).getEndVertex().getRadius() * Math.cos(k));
                line.setEndY(lineList.get(j).getEndVertex().getY() + lineList.get(j).getEndVertex().getRadius() * Math.sin(k));
            } else {
                line.setEndX(lineList.get(j).getEndVertex().getX() - lineList.get(j).getEndVertex().getRadius() * Math.cos(k));
                line.setEndY(lineList.get(j).getEndVertex().getY() - lineList.get(j).getEndVertex().getRadius() * Math.sin(k));
            }
            CreateRis(line.getEndX(), line.getEndY(), line, line1, line2);



            root.getChildren().addAll(line, line1, line2);
        }
        for (int j = 0; j < vertexList.size(); j++) {
            final Circle circle = createCircle("Circle", javafx.scene.paint.Color.BLUE, vertexList.get(j).getRadius());
            circle.setTranslateX(vertexList.get(j).getX());
            circle.setTranslateY(vertexList.get(j).getY());
            root.getChildren().add(circle);
            System.out.println("Success!");
        }

    }

    private Circle createCircle(final String name, final javafx.scene.paint.Color color, int radius) {
        //create a circle with desired name,  color and radius
        final Circle circle = new Circle(radius, new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[]{
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

                //the event will be passed only to the circle which is on front
                me.consume();
            }
        });



                /*double dragX = me.getSceneX() - dragAnchor.getX();
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
                // showOnConsole(name + " was dragged (x:" + dragX + ", y:" + dragY +")");*/

//создаем новую линии:основную и две для уголка
        circle.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                final Line line = new Line();
                final Line line1 = new Line();
                final Line line2 = new Line();
                final Double x = me.getSceneX();
                final Double y = me.getSceneY();
                line.setStartX(me.getSceneX());
                line.setStartY(me.getSceneY());
                line.setEndX(me.getSceneX());
                line.setEndY(me.getSceneY());
                line.onDragEnteredProperty();
                line.setFill(null);
                line.setStroke(javafx.scene.paint.Color.RED);
                line.setStrokeWidth(2);
                final ILine lin = new ILine();

//рисуем линию и уголок в след за курсором
                circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {

                        line.setEndX(me.getSceneX());
                        line.setEndY(me.getSceneY());
                        CreateRis(me.getSceneX(), me.getSceneY(), line, line1, line2);


                    }
                });

//создаем стрелку
                circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {

                        CreateArrow(me, line, lin, line1, line2, x, y);
                    }
                });         // show the line shape;
                root.getChildren().add(line1);
                root.getChildren().add(line2);
                root.getChildren().add(line);
            }
        });


        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //change the z-coordinate of the circle
                circle.toFront();
                //showOnConsole("Mouse entered " + name);
            }
        });


        circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //showOnConsole("Mouse released above " +name);
            }
        });


        return circle;
    }

    //функция создания стрелки:использую два цикла:1)для проверки,лежит ли конец стрелки в круге, вычисления точки пересечения с кругом.
// 2)вычисление для начала стрелки
    private void CreateArrow(MouseEvent me, Line line, ILine lin, Line line1, Line line2, Double x, Double y) {
        for (Vertex vert : vertexList) {

            if (Math.pow((me.getSceneX() - vert.getX()), 2) + Math.pow((me.getSceneY() - vert.getY()), 2) < Math.pow(vert.getRadius(), 2)) {


                for (Vertex vert1 : vertexList) {
                    if (Math.pow((line.getStartX() - vert1.getX()), 2) + Math.pow((line.getStartY() - vert1.getY()), 2) < Math.pow(vert.getRadius(), 2)) {
                        line.setStartX(vert1.getX());
                        line.setStartY(vert1.getY());
                        lin.setStartVertex(vert1);
                        lineList.add(lin);
                        Double k = Math.atan((vert1.getY() - vert.getY()) / (vert1.getX() - vert.getX()));
                        if (vert1.getX() > vert.getX()) {
                            line.setEndX(vert.getX() + vert.getRadius() * Math.cos(k));
                            line.setEndY(vert.getY() + vert.getRadius() * Math.sin(k));

                        } else {
                            line.setEndX(vert.getX() - vert.getRadius() * Math.cos(k));
                            line.setEndY(vert.getY() - vert.getRadius() * Math.sin(k));


                        }
                        CreateRis(line.getEndX(), line.getEndY(), line, line1, line2);
                        lin.setEndVertex(vert);
                        break;
                    }

                }
                break;
            } else {

                line.setEndX(x);
                line.setEndY(y);
                line1.setStartX(x);
                line1.setEndX(x);
                line1.setStartY(y);
                line1.setEndY(y);
                line2.setStartX(x);
                line2.setEndX(x);
                line2.setStartY(y);
                line2.setEndY(y);
            }

        }
    }

    //рисование самого уголка
    private void CreateRis(Double X, Double Y, Line line, Line _line1, Line _line2) {

        double beta = Math.atan2(Y - line.getStartY(), X - line.getStartX()); //{ArcTan2 ищет арктангенс от x/y что бы неопределенностей не
        //  возникало типа деления на ноль}
        double alfa = Math.PI / 15;// {угол между основной осью стрелки и рисочки в конце}
        int r1 = 20; //{длинна риски}

        int x1 = (int) Math.round(X - r1 * Math.cos(beta + alfa));
        int y1 = (int) Math.round(Y - r1 * Math.sin(beta + alfa));
        int x2 = (int) Math.round(X - r1 * Math.cos(beta - alfa));
        int y2 = (int) Math.round(Y - r1 * Math.sin(beta - alfa));


        _line1.setStartX(X);
        _line1.setStartY(Y);
        _line1.setEndX(x1);
        _line1.setEndY(y1);
        _line1.onDragEnteredProperty();
        _line1.setFill(null);
        _line1.setStroke(javafx.scene.paint.Color.RED);
        _line1.setStrokeWidth(2);


        _line2.setStartX(X);
        _line2.setStartY(Y);
        _line2.setEndX(x2);
        _line2.setEndY(y2);
        _line2.onDragEnteredProperty();
        _line2.setFill(null);
        _line2.setStroke(javafx.scene.paint.Color.RED);
        _line2.setStrokeWidth(2);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
