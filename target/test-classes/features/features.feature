

Feature: Title of your feature
  I want to use this template for my feature file

 
  Scenario: check the List of departments in HR SCHEMA which are not in USA 
    
    And get the details from dropdown menu 
    When get the details from db
    Then compare the details from dropdown menu and db
    
    
    Scenario: check the webtable in website  displays the details of total number of departments present in each city
    
    And get the details from the webtable 
    When get the details from db using query
    Then compaer the details from webtable and db 
    
    Scenario: check the details of the employee who is earning the 3rd highest salary
    
   And get the details of webtable from webpage 
   When get the details from Employees db
   Then compare the employee details from webtable and employee dp 
    