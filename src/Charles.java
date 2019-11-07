import java.util.Random;

class Charles implements Runnable {

  Garden garden;
  Random rand = new Random();

  public Charles(Garden g) {
    this.garden = g;
  }

  public void run() {
    try {
      Thread.sleep(rand.nextInt(1000)); // makes the execution more random
      for (int i = 0; i < 10; i++) {

        garden.waitToPlant();
        garden.Plant();
        Thread.sleep(rand.nextInt(500)); // Planting every 500 millisecond
      }
    } catch (InterruptedException e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(1);
    }
  }

}