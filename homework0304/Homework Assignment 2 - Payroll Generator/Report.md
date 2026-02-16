# Report for Payroll Generator

This report helps you demonstrate your understanding of the concepts. You should write this report after you have completed the project. 

## Technical Questions

1. What does CSV stand for? 
   Comma Separated Values

2. Why would you declare `List<IEmployee>` instead of `ArrayList<HourlyEmployee>`?
   Because List<IEmployee> lets you store any employee that implements IEmployee (HourlyEmployee, SalaryEmployee). It supports polymorphism and keeps code flexible.
3. When you have one class referencing another object, such as storing that object as one of the attributes of the first class - what type of relationship is that called (between has-a and is-a)?
That is a has-a relationship.
4. Can you provide an example of a has-a relationship in your code (if one exists)?
pay stubs in List<IPayStub>

5. Can you provide an example of an is-a relationship in your code (if one exists)?
HourlyEmployee is-a AbstractEmployee (inheritance).

6. What is the difference between an interface and an abstract class?
Interface: defines a contract (method signatures); there is no implementation in the interface.

Abstract class: can define both a contract and shared implementation + shared fields; subclasses inherit behavior and can override parts.

7. What is the advantage of using an interface over an abstract class?
A class can implement multiple interfaces but can extend only one class. Interfaces are better when you want a common behavior contract across unrelated class hierarchies.

8. Is the following code valid or not? `List<int> numbers = new ArrayList<int>();`, explain why or why not. If not, explain how you can fix it. 

Not valid. Java generics require reference types, and int is a primitive.

9. Which class/method is described as the "driver" for your application? 

student.PayrollGenerator — specifically its public static void main(String[] args) method.

10. How do you create a temporary folder for JUnit Testing? 
@TempDir

## Deeper Thinking 

Salary Inequality is a major issue in the United States. Even in STEM fields, women are often paid less for [entry level positions](https://www.gsb.stanford.edu/insights/whats-behind-pay-gap-stem-jobs). However, not paying equal salary can hurt representation in the field, and looking from a business perspective, can hurt the company's bottom line has diversity improves innovation and innovation drives profits. 

Having heard these facts, your employer would like data about their salaries to ensure that they are paying their employees fairly. While this is often done 'after pay' by employee surveys and feedback, they have the idea that maybe the payroll system can help them ensure that they are paying their employees fairly. They have given you free reign to explore this idea.

Think through the issue / making sure to cite any resources you use to help you better understand the topic. Then write a paragraph on what changes you would need to make to the system. For example, would there be any additional data points you would need to store in the employee file? Why? Consider what point in the payroll process you may want to look at the data, as different people could have different pretax benefits and highlight that. 

The answer to this is mostly open. We ask that you cite at least two sources to show your understanding of the issue. The TAs will also give feedback on your answer, though will be liberal in grading as long as you show a good faith effort to understand the issue and making an effort to think about how your design to could help meet your employer's goals of salary equity. 
To help detect and prevent pay inequity, I would extend the system to store additional job-related attributes that are relevant for fair pay analysis, not just payroll processing. For example: job level/grade, role title, department, location (or pay region), hire date/tenure, employment type (full/part-time), and a standardized performance band.
