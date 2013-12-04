package mediasManagement;


public enum TypeMedia 
{
	BOOK(1,"Book"), 
	DVD(2,"DVD");
	private final Integer value;
  private final String label;
  private TypeMedia(Integer value, String label) {
      this.value = value;
      this.label = label;
  }
  public Integer value() { return value; }
  public String label() { return label; }
  
  /**
   * Get the type of media according to its value
   * @param value
   * @return
   */
  public static TypeMedia fromValue(Integer value) {
      switch (value) {
      case 1: return BOOK;
      case 2: return DVD;
      default: return null;
      }
  }
}
