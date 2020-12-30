package mypackage;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.BufferedWriter;		
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AmazonCheckoutBot {
	
	public static boolean existsElement(String id, WebDriver driver) {
	    try {
	        driver.findElement(By.id(id));
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	    return true;
	}
	
	//@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the Amazon product url: ");
		String url = input.nextLine();
		System.out.print("Enter your Amazon username: ");
		String username = input.nextLine();
		System.out.print("Enter your Amazon password: ");
		String passwd = input.nextLine();
		
		System.out.println("url: " + url);
		System.out.println("name: " + username);
		System.out.println("pass: " + passwd);

		System.setProperty("webdriver.chrome.driver","/Users/sethamballa/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		String baseUrl = url;
		driver.get(baseUrl);
		
		WebElement signInFirst = driver.findElement(By.id("nav-link-accountList"));
		signInFirst.click();
		
		WebElement email = driver.findElement(By.name("email"));
		email.sendKeys(username);

		WebElement continue1 = driver.findElement(By.id("continue"));
		continue1.click();

		WebElement pass = driver.findElement(By.name("password"));
		pass.sendKeys(passwd);

		WebElement signIn = driver.findElement(By.id("signInSubmit"));
		signIn.click();
		
		//getCookies(driver);
		
		WebElement available = driver.findElement(By.id("availability"));
		while(available.getText().equals("Currently unavailable.\n" + 
				"We don't know when or if this item will be back in stock.")) {
			
			System.out.println(available.getText());
			TimeUnit.SECONDS.sleep(4);
			available = driver.findElement(By.id("availability"));
			
		}
		System.out.println(available.getText());

		List<WebElement> addToCart = driver.findElements(By.id("add-to-cart-button"));
		if(addToCart.size() > 0) {
			addToCart.get(0).click();
		}else {
			WebElement options = driver.findElement(By.id("buybox-see-all-buying-choices-announce"));
			options.click();
			
			WebDriverWait wait = new WebDriverWait(driver, 5);
		    WebElement addToCart1 = wait.until(ExpectedConditions.elementToBeClickable(By.id("a-autoid-2-announce")));
			addToCart1.click();

		}
		

		WebElement proceedCheckout = driver.findElement(By.id("hlb-ptc-btn-native"));
		proceedCheckout.click();
		
		
		WebElement placeOrder = driver.findElement(By.name("placeYourOrder1"));
		//placeOrder.click();

		WebElement close = driver.findElement(By.className("a-button-inner"));
		close.click();

		input.close();
		driver.quit();
	}


	@SuppressWarnings("deprecation")
	public static void addCookie(WebDriver driver) {
		try{			

			File file = new File("Cookies.data");							
			FileReader fileReader = new FileReader(file);							
			BufferedReader Buffreader = new BufferedReader(fileReader);							
			String strline;			
			while((strline = Buffreader.readLine())!=null){									
				StringTokenizer token = new StringTokenizer(strline,";");									
				while(token.hasMoreTokens()){					
					String name = token.nextToken();					
					String value = token.nextToken();					
					String domain = token.nextToken();					
					String path = token.nextToken();					
					Date expiry = null;					
					String val;			
					if(!(val=token.nextToken()).equals("null")){		
						expiry = new Date(val);					
					}		
					System.out.println("name: " + name);
					System.out.println("value: " + value);
					System.out.println("domain: " + domain);
					System.out.println("path: " + path);

					Boolean isSecure = new Boolean(token.nextToken()).booleanValue();		
					Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);			
					System.out.println(ck);
					driver.manage().addCookie(ck); // This will add the stored cookie to your current session					
				}		
			}
			Buffreader.close();
		}catch(Exception ex){					
			ex.printStackTrace();
		}	

	}
	public static void getCookies(WebDriver driver) {
		File file = new File("Cookies.data");							
		try		
		{	  
			// Delete old file if exists
			file.delete();		
			file.createNewFile();			
			FileWriter fileWrite = new FileWriter(file);							
			BufferedWriter Bwrite = new BufferedWriter(fileWrite);							

			for(Cookie ck : driver.manage().getCookies())							
			{			
				Bwrite.write((ck.getName()+";"+ck.getValue()+";"+ck.getDomain()+";"+ck.getPath()+";"+ck.getExpiry()+";"+ck.isSecure()));																									
				Bwrite.newLine();             
			}			
			Bwrite.close();			
			fileWrite.close();	

		}
		catch(Exception ex)					
		{		
			ex.printStackTrace();			
		}		

	}

}