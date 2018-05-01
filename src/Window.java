import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener {
	
	private JButton bouton;
	private JButton bouton2;
	
	public Window() {
		super();
		build();
	}

	private void build(){
		setTitle("DomoDoor"); //On donne un titre à l'application
		setSize(400,200); //On donne une taille à notre fenêtre
		setLocationRelativeTo(null); //On centre la fenêtre sur l'écran
		setResizable(false); //On interdit la redimensionnement de la fenêtre
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //On dit à l'application de se fermer lors du clic sur la croix
		setContentPane(buildContentPane());
	}
 
 
	private JPanel buildContentPane(){
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
 
		bouton = new JButton("Ouvrir la porte!");
		panel.add(bouton);
 
		bouton2 = new JButton("Fermer la porte!");
		panel.add(bouton2);
 
		return panel;
	}
 
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 
		if(source == bouton){
			System.out.println("Vous avez cliqué ici.");
		} else if(source == bouton2){
			System.out.println("Vous avez cliqué là.");	
		}
	}
}
