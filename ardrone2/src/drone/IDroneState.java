package drone;

public interface IDroneState {

	// avancer
	public void forward(Controller controller);

	// Reculer
	public void backward(Controller controller);

	// Deplacement horizontal a droite
	public void horizontalRight(Controller controller);

	// Deplacement horizontal a gauche
	public void horizontalLeft(Controller controller);

	// Decoller
	public void takeOff(Controller controller);

	// Atterir
	public void landing(Controller controller);

	// Pivoter a droite
	public void rotateRight(Controller controller);

	// Pivoter a gauche
	public void rotateLeft(Controller controller);

	// Descendre
	public void goDown(Controller controller);

	// Monter
	public void goUp(Controller controller);

	// Calibrer
	public void calibrate(Controller controller);

	// Flit en avant
	public void frontFlip(Controller controller);

	// Flip en arriere
	public void backFlip(Controller controller);

	// Flip a gauche
	public void leftFlip(Controller controller);

	// Flip a droit
	public void rightFlip(Controller controller);

}
