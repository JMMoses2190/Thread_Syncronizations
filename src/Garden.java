import java.util.concurrent.locks.*;


public class Garden {


  //Counters to keep track of holes dug, planted and filled
  int openHoles = 0;
  int filledHoles = 0;
  int plantedHoles = 0;
  int count = 0;

  final Lock lock = new ReentrantLock();

  //Locks for digging holes
  final Condition noHolesDug = lock.newCondition();
  final Condition holesDug = lock.newCondition();

  //Locks for filling holes
  final Condition noHolesFull = lock.newCondition();
  final Condition holesFull = lock.newCondition();

  //Locks for planting
  final Condition noPlants = lock.newCondition();
  final Condition fullPlants = lock.newCondition();

  public void waitToDig() throws InterruptedException {
    //Jordan can only have 5 open holes at once
    if (openHoles >= 5) {
      System.out.println("Jordan is waiting to dig a hole.");
    }
    lock.lock();

    try {
      //If more than 5 holes are open, Jordan Waits
      while (openHoles >= 5) {
        holesDug.await();
      }
      //Signal for planting to start
      noPlants.signal();
    } finally {
      lock.unlock();
    }

  }

  public void dig() throws InterruptedException {

    lock.lock();

    try {
      //Wait if there are 5 opens holes
      while (openHoles == 5) {
        holesDug.await();
      }

      openHoles++;
      System.out.println("Jordan dug a hole. " + "\t\t\t\t\t\t\t\t\t" + ++count);

      holesDug.signal();


    } finally {
      lock.unlock();
    }
  }


  public void waitToPlant() throws InterruptedException {

    //if there are no open holes, Charles can not plant
    if (openHoles == 0) {
      System.out.println("Charles is waiting to plant a hole");
    }
    lock.lock();

    try {
      //When 10 holes are planted, Charles is done
      while (plantedHoles == 10) {
        fullPlants.await();
      }

    } finally {
      lock.unlock();
    }

  }


  public void Plant() throws InterruptedException {

    lock.lock();

    try {
      //If there are no open holes, Charles waits
      while (openHoles == 0) {
        noPlants.await();
      }

      //Decrement open holes by 1 when Charles plants one
      openHoles--;
      System.out.println("Charles planted a hole." + "\t\t\t\t\t\t" + ++plantedHoles);

      //send signal is no holes are open
      noHolesDug.signal();
      //send signal if 10 holes are dug
      holesDug.signal();
      //Send signal for holes to be filled
      noHolesFull.signal();
      //Send signal if all holes are full
      holesFull.signal();
    } finally {
      lock.unlock();

    }

  }

  public void waitToFill() throws InterruptedException {

    //If there are no planted holes or if there are 10 open holes, Tracy waits
    if (plantedHoles == 0) {
      System.out.println("Tracy is waiting to fill a hole");
    } else if (openHoles == 10) {
      System.out.println("Tracy is waiting to fill a hole");

    }

    lock.lock();
    try {
      //If the amount of holes filled is equal to hole planted, Tracy waits
      while (filledHoles == plantedHoles) {
        holesFull.await();
      }


    } finally {
      lock.unlock();

    }

  }

  public void fill() throws InterruptedException {

    lock.lock();

    try {
      //If there are no holes planted, Tracy waits
      while (plantedHoles == 0) {
        noHolesFull.await();
      }

      System.out.println("Tracy filled a hole." + "\t\t\t\t\t\t" + ++filledHoles);
      //send signal When the hole is filled
      holesFull.signal();

    } finally {
      lock.unlock();

    }

  }
}