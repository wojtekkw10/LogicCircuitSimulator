/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package InternalModel;

import java.util.ArrayList;

public class App {

    public static void main(String[] args) {
        Simulation s = new Simulation();

        ArrayList<Integer> a = new ArrayList<>();
        a.add(5);
        //System.out.println(a.get(-1));

        System.out.println(s.arrayWireGrid);
        s.simulate(1);
        System.out.println(s.arrayWireGrid);
    }
}
