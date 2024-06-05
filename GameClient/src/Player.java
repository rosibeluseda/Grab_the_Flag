import java.awt.Point;

public class Player {
	
	public enum PlayerStatus {
		DEFENSE, 
		ATTACKING, 
		DAMAGE,
		DEAD,
		WIN
		}
	private String username;                          //Define the attributes of the class player
	private boolean isFlag;
	private short lives;
	private Point position;
	private short team;
	private short radius;
	private PlayerStatus status;
	
	
	public Player() {
		username = "";                          //Define the attributes of the class player
		isFlag = false;
		lives = 0;
		position = null;
		team = 0;
		radius = 0;
		status = PlayerStatus.DEFENSE;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String userName) {
		this.username = userName;
	}
	public boolean isFlag() {
		return isFlag;
	}
	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}
	public short getLives() {
		return lives;
	}
	public void setLives(short lives) {
		this.lives = lives;
	}
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public short getTeam() {
		return team;
	}

	public void setTeam(short team) {
		this.team = team;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(short radio) {
		this.radius = radio;
	}

    public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}
	
	public Player createPlayer(String username, boolean isFlag, short lives, Point position, short team, short radius, String status)
	{
		this.username = username;                         
		this.isFlag = isFlag;
		this.lives = lives;
		this.position = position;
		this.team = team;
		this.radius = radius;
		this.status = PlayerStatus.valueOf(status);
		return this;
	}

}
