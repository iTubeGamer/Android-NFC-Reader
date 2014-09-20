package net.gymhark;

public class ScanTag {
	private long id;
	private int schuelerid;
	private long time;
	private String ergebnis;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getschuelerid() {
		return schuelerid;
	}

	public void setschuelerid(int schuelerid) {
		this.schuelerid = schuelerid;
	}
	
	public long gettime() {
		return time;
	}
	

	public void settime(long time) {
		 this.time = time;
	}
	
	public String toString() {
		ergebnis = "Schueler-ID: " + String.valueOf(this.schuelerid) + ", Zeit: " + String.valueOf(this.time);
		return ergebnis;
	}
}