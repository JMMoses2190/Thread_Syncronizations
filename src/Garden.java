import java.util.concurrent.locks.*;


public class Garden {

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
    if (openHoles >= 5) {
      System.out.println("Jordan is waiting to dig a hole.");
    }
    lock.lock();

    try {
      while (openHoles >= 5) {
        holesDug.await();
      }
      noPlants.signal();
    } finally {
      lock.unlock();
    }

  }

  public void dig() throws InterruptedException {

    lock.lock();

    try {
      while (openHoles == 5) {
        holesDug.await();
      }

      openHoles++;
      System.out.println("Jordan dug a hole. " + "\t\t\t\t" + ++count);

      holesDug.signal();


    } finally {
      lock.unlock();
    }
  }


  public void waitToPlant() throws InterruptedException {

    if (openHoles == 0) {
      System.out.println("Charles is waiting to plant a hole");
    }
    lock.lock();

    try {
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
      while (openHoles == 0) {
        noPlants.await();
      }

      openHoles--;
      System.out.println("Charles planted a hole." + "\t\t\t" + ++plantedHoles);


      noHolesDug.signal();
      holesDug.signal();
      noHolesFull.signal();
      holesFull.signal();
    } finally {
      lock.unlock();

    }

  }

  public void waitToFill() throws InterruptedException {
    if (plantedHoles == 0) {
      System.out.println("Tracy is waiting to plant a hole");
    } else if(openHoles == 10){
      System.out.println("Tracy is waiting to plant a hole");

    }

    lock.lock();
    try {
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

      while (plantedHoles == 0) {
        noHolesFull.await();
      }

      System.out.println("Tracy filled a hole." + "\t\t" + ++filledHoles);

      holesFull.signal();

    } finally {
      lock.unlock();

    }

  }
}