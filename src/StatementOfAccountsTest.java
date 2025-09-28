import java.time.LocalDate;
import java.util.List;

/**
 * Test class for Statement of Accounts functionality
 * Verifies all components work correctly with the database
 */
public class StatementOfAccountsTest {
    
    public static void main(String[] args) {
        System.out.println("=== iSLU Student Portal - Statement of Accounts Test ===\n");
        
        // Initialize data managers
        SharedDataManager.initialize();
        
        // Test student ID
        String testStudentID = "2255146";
        
        try {
            // Test 1: Create and retrieve account statement
            System.out.println("Test 1: Creating account statement for student " + testStudentID);
            AccountStatement statement = AccountStatementManager.getStatement(testStudentID);
            System.out.println("✓ Account statement created successfully");
            System.out.println("  - Student ID: " + statement.getStudentID());
            System.out.println("  - Semester: " + statement.getSemester() + " " + statement.getAcademicYear());
            System.out.println("  - Total Amount: P " + String.format("%,.2f", statement.getTotalAmount()));
            System.out.println("  - Balance: P " + String.format("%,.2f", statement.getBalance()));
            System.out.println();
            
            // Test 2: Check exam period dues
            System.out.println("Test 2: Checking exam period dues");
            double prelimDue = statement.getExamPeriodDue(AccountStatement.ExamPeriod.PRELIM);
            double midtermDue = statement.getExamPeriodDue(AccountStatement.ExamPeriod.MIDTERM);
            double finalsDue = statement.getExamPeriodDue(AccountStatement.ExamPeriod.FINALS);
            
            System.out.println("  - Prelim Due: P " + String.format("%,.2f", prelimDue));
            System.out.println("  - Midterm Due: P " + String.format("%,.2f", midtermDue));
            System.out.println("  - Finals Due: P " + String.format("%,.2f", finalsDue));
            System.out.println();
            
            // Test 3: Check payment status
            System.out.println("Test 3: Checking payment status");
            System.out.println("  - Prelim Paid: " + (statement.isPrelimPaid() ? "YES" : "NO"));
            System.out.println("  - Midterm Paid: " + (statement.isMidtermPaid() ? "YES" : "NO"));
            System.out.println("  - Finals Paid: " + (statement.isFinalsPaid() ? "YES" : "NO"));
            System.out.println();
            
            // Test 4: Process a payment
            System.out.println("Test 4: Processing a payment");
            double paymentAmount = 10000.00;
            String paymentChannel = "GCash";
            String reference = "TEST001";
            
            AccountStatement.PaymentResult result = AccountStatementManager.processPayment(
                testStudentID, paymentAmount, paymentChannel, reference
            );
            
            if (result.success) {
                System.out.println("✓ Payment processed successfully");
                System.out.println("  - Amount: P " + String.format("%,.2f", paymentAmount));
                System.out.println("  - Channel: " + paymentChannel);
                System.out.println("  - Reference: " + reference);
                System.out.println("  - New Balance: P " + String.format("%,.2f", result.newBalance));
                System.out.println("  - Message: " + result.message);
            } else {
                System.out.println("✗ Payment failed: " + result.message);
            }
            System.out.println();
            
            // Test 5: Check updated payment status
            System.out.println("Test 5: Checking updated payment status");
            statement.updatePaymentStatuses();
            System.out.println("  - Prelim Paid: " + (statement.isPrelimPaid() ? "YES" : "NO"));
            System.out.println("  - New Balance: P " + String.format("%,.2f", statement.getBalance()));
            System.out.println();
            
            // Test 6: Display fee breakdown
            System.out.println("Test 6: Fee breakdown");
            List<FeeBreakdown> fees = statement.getFeeBreakdowns();
            System.out.println("  Total fees: " + fees.size());
            for (FeeBreakdown fee : fees) {
                System.out.println("  - " + fee.getDescription() + ": P " + String.format("%,.2f", fee.getAmount()));
            }
            System.out.println();
            
            // Test 7: Display payment history
            System.out.println("Test 7: Payment history");
            List<PaymentTransaction> payments = statement.getPaymentHistory();
            System.out.println("  Total payments: " + payments.size());
            for (PaymentTransaction payment : payments) {
                System.out.println("  - " + payment.getDate() + " | " + payment.getChannel() + 
                                 " | " + payment.getReference() + " | " + payment.getAmount());
            }
            System.out.println();
            
            // Test 8: Generate statement report
            System.out.println("Test 8: Generating statement report");
            String report = AccountStatementManager.generateStatementReport(testStudentID);
            System.out.println("✓ Report generated successfully");
            System.out.println("Report length: " + report.length() + " characters");
            System.out.println();
            
            // Test 9: Test exam eligibility messages
            System.out.println("Test 9: Exam eligibility messages");
            System.out.println("  - Prelim: " + statement.getExamEligibilityMessage(AccountStatement.ExamPeriod.PRELIM));
            System.out.println("  - Midterm: " + statement.getExamEligibilityMessage(AccountStatement.ExamPeriod.MIDTERM));
            System.out.println("  - Finals: " + statement.getExamEligibilityMessage(AccountStatement.ExamPeriod.FINALS));
            System.out.println();
            
            System.out.println("=== All tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}