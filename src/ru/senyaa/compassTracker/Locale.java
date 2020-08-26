package ru.senyaa.compassTracker;

public class Locale {	
	private static String[] russianText = new String[20];
	private static String[] englishText = new String[20];
	
	public static String[] localText = new String[20];
	
	private static void genLocales() {
		
		englishText[0] = "Usage: /compass get/remove or /compass set NICKNAME";
		englishText[1] = "/compass get";
		englishText[2] = "/compass get";
		englishText[3] = "Overworld";
		englishText[4] = "Nether";
		englishText[5] = "End";
		englishText[6] = " gone to the ";
		englishText[7] = "Target location: ";
		englishText[8] = "Compass is tracking: ";
		englishText[9] = "Hold a compass in your main hand";
		englishText[10] = "No compass to set up! Use ";
		englishText[11] = " to get one!";
		englishText[12] = "Player not found! ";
		englishText[13] = "You got a compass! ";
		englishText[14] = "You already have a compass!";
		englishText[15] = "Compass removed!";
		englishText[16] = "Use ";
		englishText[17] = " to setup the compass";
		englishText[18] = "Tracking compass";
		englishText[19] = " not found";
		
		russianText[0] = "Использование: /compass get/remove или /compass set ИМЯ";
		russianText[1] = "/compass get";
		russianText[2] = "/compass set";
		russianText[3] = "Верхний мир";
		russianText[4] = "Ад";
		russianText[5] = "Энд";
		russianText[6] = " перешёл в ";
		russianText[7] = "Координаты: ";
		russianText[8] = "Компас отслеживает: ";
		russianText[9] = "Держи компас в основной руке";
		russianText[10] = "Компас не найден. Используй";
		russianText[11] = " чтобы получить компас.";
		russianText[12] = "Игрок не найден";
		russianText[13] = "Компас получен";
		russianText[14] = "Уже имеется компас";
		russianText[15] = "Компас удалён";
		russianText[16] = "Используй ";
		russianText[17] = " чтобы настроить компас";
		russianText[18] = "Отслеживающий компас";
		russianText[19] = " не найден";
	}
	
	public static void setRussian() {
		genLocales();
		localText = russianText;
	}
	
	public static void setEnglish() {
		genLocales();
		localText = englishText;
	}
}
