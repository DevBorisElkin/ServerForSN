import database.Database;

public class SubThread implements Runnable{
    boolean activation=true;
    Thread thread;
    Server server;

    public SubThread(Server server) {
        thread=new Thread(this);
        this.server=server;
        thread.setDaemon(true);
    }

    public void start(){
        if(activation){
            activation=false;
            thread.start();
        }
    }

    @Override
    public void run() {
        while (true){
            try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
            Database.checkAllOnline();
            server.sendUpdatedData();
        }
    }
}
