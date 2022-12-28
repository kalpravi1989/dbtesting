package features;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import base.DBreader;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
public class StepDefinition {
	private WebDriver driver = Hooks.driver;
	List<String> dropdown = new ArrayList<String>();
	List<String> rowdataq1 = new ArrayList<String>();
	Map<String, String> dep = new HashMap<String, String>();
	Map<String, String> dbdepmap = new HashMap<String, String>();
	ArrayList<ArrayList<String>> uiDep = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> dbDep = new ArrayList<ArrayList<String>>();
	int rowcount = 0;
	int colcount2 = 0;
	ArrayList<String> empDetails = new ArrayList<String>();
	ArrayList<String> dbEmpDetails = new ArrayList<String>();


	@Given("^get the details from dropdown menu$")
	public void get_the_details_from_dropdown_menu() throws Throwable {

		driver.findElement(By.xpath("//select[@name='departments']")).click();
		List<WebElement> dropdownElement = driver.findElements(By.xpath("//select[@name='departments']/option"));
		for (WebElement e : dropdownElement) {
			dropdown.add(e.getText());
		}
		System.out.println(dropdown);
	}

	@When("^get the details from db$")
	public void get_the_details_from_db() throws Throwable {
		String query = "select department_name from departments where location_id in (select location_id from locations where country_id!='US')";
		DBreader db = new DBreader();
		ResultSet resultSet = db.dbreader(query);
		ResultSetMetaData rsMetaData = resultSet.getMetaData();
		int colcount = rsMetaData.getColumnCount();

		while (resultSet.next()) {
			for (int x = 1; x <= colcount; x++) {
				rowdataq1.add(resultSet.getString(x));

			}

		}
		System.out.println(rowdataq1);


	}

	@Then("^compare the details from dropdown menu and db$")
	public void compare_the_details_from_dropdown_menu_and_db() throws Throwable {
		for (int i = 0; i < dropdown.size(); i++) {
			if (!(rowdataq1.contains(dropdown.get(i)))) {
				System.out.println(dropdown.get(i));
			}
		}
	}

	@Given("^get the details from the webtable$")
	public void get_the_details_from_the_webtable() throws Throwable {
		List<WebElement> tablerow = driver.findElements(
				By.xpath("//table[@class='adap-table']/tbody/tr/td/span[contains(text(),'City')]/parent::td"));
		int i = 0;

		for (WebElement e : tablerow) {
			uiDep.add((new ArrayList<String>()));
			String cityname = e.getText();
			WebElement depNum = driver.findElement(By.xpath(
					"//table[@class='adap-table']/tbody/tr/td/span[contains(text(),'City')]/parent::td[contains(text(),'"
							+ cityname + "')]/preceding-sibling::td"));
			String depcount = depNum.getText();
			uiDep.get(i).add(cityname);
			uiDep.get(i).add(depcount);
			i++;
			dep.put(cityname, depcount);
		}



		System.out.println(uiDep);
	}

	@When("^get the details from db using query$")
	public void get_the_details_from_db_using_query() throws Throwable {
		DBreader db = new DBreader();
		String query = "select distinct(l.city),count(d.department_name) from locations l join departments d on l.location_id=d.location_id group by l.city,d.department_name order by l.city";
		ResultSet resultSet = db.dbreader(query);
		ResultSetMetaData rsMetaData = resultSet.getMetaData();
		colcount2 = rsMetaData.getColumnCount();
		while (resultSet.next()) {
			dbDep.add((new ArrayList<String>()));
			for (int x = 1; x <= colcount2; x++) {
				dbDep.get(rowcount).add(x - 1, resultSet.getString(x));

			}
			rowcount++;
		}int i=0;
		while (i < rowcount) {
			dbdepmap.put(dbDep.get(i).get(0), dbDep.get(i).get(1));
				
				i++;
			}

		System.out.println(dbdepmap);

	}

	@Then("^compaer the details from webtable and db$")
	public void compaer_the_details_from_webtable_and_db() throws Throwable {
		for (Entry<String, String> entry : dbdepmap.entrySet()) {
			if (dep.containsKey(entry.getKey())) {
				if (entry.getValue().equals(dep.get(entry.getKey()))) {

				}
			} else if (!(dep.containsKey(entry.getKey()))) {
				System.out.println("UI webtable is not contain");
				System.out.println(entry.getKey() + " " + entry.getValue());

			}
		}
	}

	@Given("^get the details of webtable from webpage$")
	public void get_the_details_of_webtable_from_webpage() throws Throwable {
		List<WebElement> ele = driver.findElements(By.xpath("//table[@class='salary_employee']/tbody/tr/td"));
		for (WebElement e : ele) {
			String s=e.getText();
			String[] str = s.split(":");
			empDetails.add(str[1]);
		}
		System.out.println(empDetails);
	}

	@When("^get the details from Employees db$")
	public void get_the_details_from_Employees_db() throws Throwable {
		DBreader db = new DBreader();
		String query = "SELECT employee_id,first_name|| ' '||last_name,salary FROM employees e1 WHERE 3-1 = (SELECT COUNT(DISTINCT salary) FROM employees e2 WHERE e2.salary > e1.salary)";
		ResultSet resultSet = db.dbreader(query);
		ResultSetMetaData rsMetaData = resultSet.getMetaData();
		int colcount = rsMetaData.getColumnCount();
		System.out.println(colcount);
		while (resultSet.next()) {
			for (int x = 1; x <= colcount; x++) {
				dbEmpDetails.add(resultSet.getString(x));

			}

		}
		System.out.println(dbEmpDetails);

	}

	@Then("^compare the employee details from webtable and employee dp$")
	public void compare_the_employee_details_from_webtable_and_employee_dp() throws Throwable {
		for (int i = 0; i < dbEmpDetails.size(); i++) {
			Assert.assertEquals(dbEmpDetails.get(i), empDetails.get(i));
		}
	}

}
