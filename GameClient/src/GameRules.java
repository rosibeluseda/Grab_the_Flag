import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class GameRules extends JFrame {

	private JPanel contentPane;
	private GameLobby lobby;

	public GameRules() {
		setBounds(100, 100, 1000, 650);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDot_8 = new JLabel("");
		lblDot_8.setOpaque(true);
		lblDot_8.setBackground(Color.YELLOW);
		lblDot_8.setBounds(76, 541, 10, 10);
		contentPane.add(lblDot_8);
		
		JLabel lblDot_7 = new JLabel("");
		lblDot_7.setOpaque(true);
		lblDot_7.setBackground(Color.YELLOW);
		lblDot_7.setBounds(76, 490, 10, 10);
		contentPane.add(lblDot_7);
		
		JLabel lblDot_6 = new JLabel("");
		lblDot_6.setOpaque(true);
		lblDot_6.setBackground(Color.YELLOW);
		lblDot_6.setBounds(76, 439, 10, 10);
		contentPane.add(lblDot_6);
		
		JLabel lblDot_5 = new JLabel("");
		lblDot_5.setOpaque(true);
		lblDot_5.setBackground(Color.YELLOW);
		lblDot_5.setBounds(76, 388, 10, 10);
		contentPane.add(lblDot_5);
		
		JLabel lblDot_4 = new JLabel("");
		lblDot_4.setOpaque(true);
		lblDot_4.setBackground(Color.YELLOW);
		lblDot_4.setBounds(76, 337, 10, 10);
		contentPane.add(lblDot_4);
		
		JLabel lblDot_3 = new JLabel("");
		lblDot_3.setOpaque(true);
		lblDot_3.setBackground(Color.YELLOW);
		lblDot_3.setBounds(76, 286, 10, 10);
		contentPane.add(lblDot_3);
		
		JLabel lblDot_2 = new JLabel("");
		lblDot_2.setOpaque(true);
		lblDot_2.setBackground(Color.YELLOW);
		lblDot_2.setBounds(76, 235, 10, 10);
		contentPane.add(lblDot_2);
		
		JLabel lblDot = new JLabel("");
		lblDot.setOpaque(true);
		lblDot.setBackground(Color.YELLOW);
		lblDot.setBounds(76, 138, 10, 10);
		contentPane.add(lblDot);
		
		JLabel lblDot_1 = new JLabel("");
		lblDot_1.setOpaque(true);
		lblDot_1.setBackground(Color.YELLOW);
		lblDot_1.setBounds(76, 184, 10, 10);
		contentPane.add(lblDot_1);
		
		JLabel lblTheRules = new JLabel("The Rules");
		lblTheRules.setHorizontalAlignment(SwingConstants.CENTER);
		lblTheRules.setForeground(Color.ORANGE);
		lblTheRules.setFont(new Font("Magneto", Font.BOLD, 28));
		lblTheRules.setBounds(386, 25, 184, 39);
		contentPane.add(lblTheRules);
		
		JLabel lblRule1 = new JLabel("Teams must have the same amount of players (2-4 per team) and one has to be the flag.");
		lblRule1.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule1.setForeground(Color.WHITE);
		lblRule1.setBounds(105, 117, 800, 40);
		contentPane.add(lblRule1);
		
		JLabel lblRule2 = new JLabel("All players will start on their side of the field, and they can move using the arrow keys.");
		lblRule2.setForeground(Color.WHITE);
		lblRule2.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule2.setBounds(105, 168, 800, 40);
		contentPane.add(lblRule2);
		
		JLabel lblRule3 = new JLabel("If a player is close to the flag, they can capture the flag by pressing the “A” key.");
		lblRule3.setForeground(Color.WHITE);
		lblRule3.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule3.setBounds(105, 219, 800, 40);
		contentPane.add(lblRule3);
		
		JLabel lblRule4 = new JLabel("A player can hit other players using the spacebar and they will expand to show the hit area.");
		lblRule4.setForeground(Color.WHITE);
		lblRule4.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule4.setBounds(105, 270, 800, 40);
		contentPane.add(lblRule4);
		
		JLabel lblRule5 = new JLabel("Players will lose a life every time they get hit. The flag player is immune.");
		lblRule5.setForeground(Color.WHITE);
		lblRule5.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule5.setBounds(105, 321, 800, 40);
		contentPane.add(lblRule5);
		
		JLabel lblRule6 = new JLabel("If a player loses all their lives, they will go to the bench zone for the remainder of the round.");
		lblRule6.setForeground(Color.WHITE);
		lblRule6.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule6.setBounds(105, 372, 800, 40);
		contentPane.add(lblRule6);
		
		JLabel lblRule7 = new JLabel("If a player captures the flag, their team wins the round.");
		lblRule7.setForeground(Color.WHITE);
		lblRule7.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule7.setBounds(105, 423, 800, 40);
		contentPane.add(lblRule7);
		
		JLabel lblRule8 = new JLabel("The team that wins 2 out of 3 rounds wins the game.");
		lblRule8.setForeground(Color.WHITE);
		lblRule8.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule8.setBounds(105, 474, 800, 40);
		contentPane.add(lblRule8);
		
		JLabel lblRule9 = new JLabel("Players can choose their teams and flag players at the start of every game.");
		lblRule9.setForeground(Color.WHITE);
		lblRule9.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRule9.setBounds(105, 525, 800, 40);
		contentPane.add(lblRule9);
		
		JLabel lblSeparation = new JLabel("");
		lblSeparation.setOpaque(true);
		lblSeparation.setHorizontalAlignment(SwingConstants.CENTER);
		lblSeparation.setForeground(Color.WHITE);
		lblSeparation.setBackground(Color.CYAN);
		lblSeparation.setBounds(3, 81, 978, 3);
		contentPane.add(lblSeparation);
		
		JButton btnClose = new JButton("CLOSE");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setFocusPainted(false);
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnClose.setForeground(new Color(102, 153, 255));
		btnClose.setBorder(new LineBorder(new Color(0, 0, 255), 3));
		btnClose.setBackground(new Color(0, 0, 0));
		btnClose.setBounds(782, 25, 154, 25);
		contentPane.add(btnClose);
		
		JLabel lblBackground = new JLabel("New label");
		lblBackground.setIcon(new ImageIcon(GameRules.class.getResource("/Media/BGRules.png")));
		lblBackground.setBounds(0, 0, 984, 611);
		contentPane.add(lblBackground);
	}
}
