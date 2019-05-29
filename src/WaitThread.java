
public class WaitThread extends Thread {

	private GUI gui;
	
	public WaitThread(GUI gui) {
		this.gui = gui;
	}
	
	/**
	 * Thread qui gere le wait
	 */
	@Override
	public void run() {
		
		int val = 0;
		
		while(val != -1) {
			
			val = gui.getProgress();
//			System.out.println(val);
			
//			gui.setLblProgress();
//			gui.lblResultProg.setText("R\u00E9sultats de la programmation : " + gui.getProgress());
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
		
//		System.out.println("end");
		
	}

}
