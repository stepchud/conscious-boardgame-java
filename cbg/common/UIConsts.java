package cbg.common;

public interface UIConsts {
	
	//size of each board space circle
	public static final int SPACE_WIDTH = 21;
	public static final int PIECE_SIZE=22;
	public static final int CARD_WIDTH=71;
	public static final int CARD_HEIGHT=96;
	
	/**
	 * Action commands
	 */
	static final String ROLL_DICE_COMMAND = "ROLL";
	static final String DRAWCARD_COMMAND = "DRAW";
	static final String BYRANDOM_COMMAND = "CHOOSERANDOM";
	static final String CHOICE_BTN_COMMAND = "LAWCHOICE";
	static final String OBEY_LAW_COMMAND = "OBEYLAW";
	static final String PLAYCARD_COMMAND = "PLAY";
	static final String KEEP_SELECTED_COMMAND = "KEEPSELECTED";
	static final String MAKE_ACE_COMMAND = "MKACE";
	static final String MAKE_XJ_COMMAND = "MKXJ";
	static final String MAKE_JO_COMMAND = "MKJO";
    static final String EAT_COMMAND = "EAT";
    static final String BREATHE_COMMAND = "BREATHE";
    static final String IMP_COMMAND = "IMP";
    static final String SHOCK_FOOD_COMMAND = "SHOCKFOOD";
    static final String SHOCK_AIR_COMMAND = "SHOCKAIR";
    static final String SELF_REMEMBER_COMMAND = "SELFREMEMBER";
    static final String TRANSFORM_COMMAND = "TRANSFORM";
    static final String END_TURN_COMMAND = "ENDTURN";
    static final String DEATH_COMMAND = "DIE!";
    static final String GIVE_BWE_COMMAND = "BWE";
    static final String GIVE_EWB_COMMAND = "EWB";
	
	/// Individual note states
	public static final byte EMPTY=0,
							CHIP=1,
							ASTRAL=2,
							ASTRAL_CHIP=3,
							MENTAL=4,
							MENTAL_CHIP=5,
							MENTAL_ASTRAL=6,
							MENTAL_ASTRAL_CHIP=7;
	
	/// Type Of Stuff
	public static final short FOOD=1,
							AIR=2,
							IMP=3;
							
	public static final Object[] ExtraAir = new Object[] {
					"Take back in as food", 
					"Draw a card"};
	
	public static final Object[] ExtraImp = new Object[] {
					"Take back in as air", 
					"Draw a card"};			
	
	public static final Object[] Shocks = new Object[] {
					"Air Shocks Food", 
					"Self-Remember",
					"Transform Emotions"};
					
	public static final Object[] Spaces = new Object[] {
					"Food",
					"Air",
					"Impression",
					"Card",
					"Law Card"};
	
	public static final Object[] SpacesAfterDeath = new Object[] {
			"Food",
			"Air",
			"Impression",
			"Card"};
			
	public static final Object[] FoodNotes = new Object[] {
					"DO768",
					"RE384",
					"MI192",
					"FA96",
					"SO48",
					"LA24",
					"TI12",
					"DO6" };
	public static final Object[] AirNotes = new Object[] {
					"DO192",
					"RE96",
					"MI48",
					"FA24",
					"SO12",
					"LA6" };
	public static final Object[] ImpNotes = new Object[] {
					"DO48",
					"RE24",
					"MI12",
					"FA6" };
	
}
