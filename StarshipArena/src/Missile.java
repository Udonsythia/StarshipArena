import java.util.ArrayList;


public class Missile extends Projectile {
	double current_velocity = 0;
	double current_turn_speed;
	
	Starship target = null;
	
	double acceleration = 0.5;
	double max_velocity = 20;
	double max_reverse_velocity = -2;
	double min_turn_velocity = 0.25;
	double max_turn_speed = 0.5;
	//weaponry
	int scan_range = 4000;

	public Missile(StarshipArena mygame, Starship newowner, String myteam, double spawnx, double spawny, double newdamage, double spawnangle, int accuracy, double newspeed, double newlifetime, int id) {
		super(mygame, newowner, myteam, spawnx, spawny, newdamage, spawnangle, accuracy, newspeed, newlifetime, id);
		max_velocity = newspeed;
	}
	
	public void destroy(Starship victim){
		model.destroy();
		game.removeProjectile(this);
		new Explosion(game, center.X(), center.Y(), 55);
//		for (int i = 0; i < 360; i += 40) {
		for (int i = 0; i < 2; i++) {
//				if(victim instanceof MissilePod){
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 0, i, 100, 20, 5, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 0, i, 100, 16, 5, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 0, i, 100, 12, 5, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 0, i, 100, 40, 5, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 0, i, 100, 32, 5, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 0, i, 100, 26, 5, 3);
//				}
//				else{
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 3, i, 100, 20, 10, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 3, i, 100, 16, 10, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 3, i, 100, 12, 10, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 3, i, 100, 40, 10, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 3, i, 100, 32, 10, 3);
//					new Projectile(game, null, "none", victim.center.x, victim.center.y, 3, i, 100, 26, 10, 3);
				int x_rand = random.nextInt(5) - 2;
				int y_rand = random.nextInt(5) - 2;
				int rand_angle = random.nextInt(360);
				
//					new Projectile(game, null, "none", center.x + x_rand, center.y + y_rand, 0, rand_angle, 100, 5, 10, 3);
				new Projectile(game, null, "none", center.x + x_rand, center.y + y_rand, 0, rand_angle, 100, 6, 4, 3);
				new Projectile(game, null, "none", center.x + x_rand, center.y + y_rand, 0, rand_angle, 100, 6, 7, 3);
				new Projectile(game, null, "none", center.x + x_rand, center.y + y_rand, 0, rand_angle, 100, 6, 10, 3);
				new Projectile(game, null, "none", center.x + x_rand, center.y + y_rand, 0, rand_angle, 100, 6, 13, 3);
//					new Projectile(game, null, team, center.x + x_rand, center.y + y_rand, 0.5, i, 100, 5, 10, 3);
//					new Projectile(game, null, team, center.x + x_rand, center.y + y_rand, 0.5, i, 100, 5, 10, 3);
		
//				}
		}
		game.addClip("sounds/effects/ex_med5.wav", -15.0f);
	}
	
	public void destroy(){
		model.destroy();
		game.removeProjectile(this);
		new Explosion(game, center.X(), center.Y(), 55);
		game.addClip("sounds/effects/ex_med5.wav", -15.0f);
		
	}
	
	public boolean setPoints(){
		current_velocity = Math.min(current_velocity + acceleration, max_velocity);
		//check if the target is already dead
		if(target != null && target.getHealth() <= 0){
			target = null;
		}
		//get a new target if fighter has no target or target is far away
		if(target == null || game.distance(center.X(), center.Y(), target.getX(), target.getY()) >= scan_range / 2){
			getClosestEnemy();
		}
		if (target != null) {
			double distance = game.distance(center.x, center.y, target.center.x, target.center.y);
			boolean positiveY = false;
			if (target.center.y >=  center.y) positiveY = true;
			double angle = Math.acos((target.center.x - center.x) / distance) * 180 / Math.PI;
			double targetAngle;
			if (positiveY) targetAngle = (270 + angle) % 360;
			else targetAngle = 270 - angle;
			//System.out.println(targetAngle);
			if (this.angle == Math.round(targetAngle)) {
				current_turn_speed = 0;
			}
			else if ((Math.round(targetAngle) - this.angle + 360) % 360 < 180) current_turn_speed = Math.min(max_turn_speed, (Math.round(targetAngle) - this.angle + 360) % 360);
			else current_turn_speed = Math.max(-max_turn_speed, -((this.angle - Math.round(targetAngle) + 360) % 360));
		}
		angle += current_turn_speed;
		Point newcenter = new Point(center.X(), center.Y()+current_velocity);
		newcenter.rotatePoint(center.X(), center.Y(), angle);
		center.setX(newcenter.X());
		center.setY(newcenter.Y()); 
		int v_index = 0;
		for (int i = 0; i < points.length; i++) {
			points[i].setX(center.X() + points[i].getXOffset());
			points[i].setY(center.Y() + points[i].getYOffset());
			points[i].rotatePoint(center.X(), center.Y(), angle);
			v_index = 2*i;
			vertices[v_index] = points[i].X();
			vertices[v_index+1] = points[i].Y();	
		}
		if(updateLifetime()){
			return true;
		}
		else{
			destroy();
			return false;
		}
	}
	
	public void getClosestEnemy(){
		if(target != null && game.distance(center.X(), center.Y(), target.getX(), target.getY()) > scan_range){
			target = null;
		}
		ArrayList<Starship> scanned = scan();
		if(scanned.size() != 0){
			for (int i = 0; i < scanned.size(); i++) {
				Starship s = scanned.get(i);
				//if fighter has no team, or scanned enemy is on another team or closer than current target
				if((team.equals("none") || !s.getTeam().equals(team)) && (target == null ||
					game.distance(center.X(), center.Y(), s.getX(), s.getY()) - getClosestBearing(s) < 
					game.distance(center.X(), center.Y(), target.getX(), target.getY()) - getClosestBearing(target))){
					 target = scanned.get(i);
				}
			}
		}
	}
	
	public ArrayList<Starship> scan(){
		ArrayList<Starship> shipsList = game.getAllShips();
		ArrayList<Starship> scanned = new ArrayList<Starship>();
		for(int i = 0; i < shipsList.size(); i++){
			if(!shipsList.get(i).equals(this) && (game.distance(center.X(), center.Y(), shipsList.get(i).getX(), shipsList.get(i).getY()) <= scan_range)){
				scanned.add(shipsList.get(i));
			}
		}
		return scanned;
	}
	
	public double getClosestBearing(Starship s){
		double relativeAngle = game.angleToPoint(center.X(), center.Y(), s.getX(), s.getY());
		double leftBearing = getTurnDistance(relativeAngle, true);
		double rightBearing = getTurnDistance(relativeAngle, false);
		if(leftBearing <= rightBearing){
			return leftBearing;
		}
		else{
			return rightBearing;
		}
	}
	
	public double getTurnDistance(double relativeAngle, boolean toLeft){
		//find which direction to turn is shortest to target
		if(angle >= relativeAngle){
			if(toLeft){
				return 360 - angle + relativeAngle; //left bearing
			}
			else{
				return angle - relativeAngle; //right bearing
			}
		}
		else if(angle < relativeAngle){
			if(toLeft){
				return relativeAngle - angle; //left bearing
			}
			else{
				 return angle + 360 - relativeAngle; //right bearing
			}
		}
		else{
			try {
				throw new GameException("error in function Missile.getTurnDistance");
			} catch (GameException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	//returns false if projectile is destroyed. Also creates explosion trail
		public boolean updateLifetime(){
			current_lifetime += 1;
//			if (current_lifetime % 2 == 0) {
//				boolean doExplosion = random.nextBoolean();
//				int rand = random.nextInt(5) - 4;
//				if (doExplosion) {
					Explosion temp = new Explosion(game, center.x, center.y, 20);
//					temp.lifetime = -2;
					temp.ticksPerFrame = random.nextInt(3);
//				}
//			}
			if(current_lifetime >= lifetime){
				return false;
			}
			return true;
		}
}
