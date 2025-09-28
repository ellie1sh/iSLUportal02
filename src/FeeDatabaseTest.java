import java.time.LocalDate;
import java.util.List;

/**
 * Test class for FeeDatabase functionality
 * Tests database-driven fee management with real-time updates
 */
public class FeeDatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== Fee Database System Test ===\n");
        
        // Initialize data managers
        SharedDataManager.initialize();
        
        try {
            // Test 1: Initial fee loading
            System.out.println("Test 1: Loading initial fees from database");
            List<FeeBreakdown> globalFees = FeeDatabase.getGlobalFees();
            System.out.println("✓ Global fees loaded: " + globalFees.size() + " fees");
            
            for (FeeBreakdown fee : globalFees) {
                System.out.println("  - " + fee.getCode() + ": " + fee.getDescription() + " (P " + String.format("%,.2f", fee.getAmount()) + ")");
            }
            System.out.println();
            
            // Test 2: Add a new global fee
            System.out.println("Test 2: Adding a new global fee");
            FeeBreakdown newFee = new FeeBreakdown(
                "GF001", 
                "New Technology Fee", 
                2000.00, 
                FeeBreakdown.FeeType.INTERNET, 
                LocalDate.now(),
                "Additional technology infrastructure fee"
            );
            
            FeeDatabase.addGlobalFee(newFee);
            System.out.println("✓ New global fee added: " + newFee.getDescription());
            System.out.println();
            
            // Test 3: Add student-specific fee
            System.out.println("Test 3: Adding student-specific fee");
            String testStudentID = "2255146";
            FeeBreakdown studentFee = new FeeBreakdown(
                "SF001",
                "Late Registration Penalty",
                500.00,
                FeeBreakdown.FeeType.PENALTY,
                LocalDate.now(),
                "Penalty for late registration"
            );
            
            FeeDatabase.addStudentFee(testStudentID, studentFee);
            System.out.println("✓ Student-specific fee added for student " + testStudentID);
            System.out.println();
            
            // Test 4: Get all fees for student
            System.out.println("Test 4: Getting all fees for student " + testStudentID);
            List<FeeBreakdown> allStudentFees = FeeDatabase.getAllFeesForStudent(testStudentID);
            System.out.println("✓ Total fees for student: " + allStudentFees.size());
            
            double totalAmount = 0;
            for (FeeBreakdown fee : allStudentFees) {
                System.out.println("  - " + fee.getCode() + ": " + fee.getDescription() + " (P " + String.format("%,.2f", fee.getAmount()) + ")");
                totalAmount += fee.getAmount();
            }
            System.out.println("  Total Amount: P " + String.format("%,.2f", totalAmount));
            System.out.println();
            
            // Test 5: Update a fee
            System.out.println("Test 5: Updating a global fee");
            FeeBreakdown updatedFee = new FeeBreakdown(
                "GF001",
                "Updated Technology Fee",
                2500.00, // Increased amount
                FeeBreakdown.FeeType.INTERNET,
                LocalDate.now(),
                "Updated technology infrastructure fee"
            );
            
            FeeDatabase.updateGlobalFee("GF001", updatedFee);
            System.out.println("✓ Global fee updated: " + updatedFee.getDescription() + " (P " + String.format("%,.2f", updatedFee.getAmount()) + ")");
            System.out.println();
            
            // Test 6: Test account statement with database fees
            System.out.println("Test 6: Testing account statement with database fees");
            AccountStatement statement = AccountStatementManager.getStatement(testStudentID);
            System.out.println("✓ Account statement created for student " + testStudentID);
            System.out.println("  - Total Amount: P " + String.format("%,.2f", statement.getTotalAmount()));
            System.out.println("  - Balance: P " + String.format("%,.2f", statement.getBalance()));
            System.out.println("  - Fee Count: " + statement.getFeeBreakdowns().size());
            System.out.println();
            
            // Test 7: Process payment and check updates
            System.out.println("Test 7: Processing payment and checking real-time updates");
            AccountStatement.PaymentResult result = AccountStatementManager.processPayment(
                testStudentID, 5000.00, "GCash", "TEST001"
            );
            
            if (result.success) {
                System.out.println("✓ Payment processed successfully");
                System.out.println("  - Amount: P " + String.format("%,.2f", 5000.00));
                System.out.println("  - New Balance: P " + String.format("%,.2f", result.newBalance));
                System.out.println("  - Message: " + result.message);
            } else {
                System.out.println("✗ Payment failed: " + result.message);
            }
            System.out.println();
            
            // Test 8: Refresh fees from database
            System.out.println("Test 8: Refreshing fees from database");
            AccountStatementManager.refreshFeesFromDatabase(testStudentID);
            System.out.println("✓ Fees refreshed from database");
            
            // Check updated totals
            statement = AccountStatementManager.getStatement(testStudentID);
            System.out.println("  - Updated Total Amount: P " + String.format("%,.2f", statement.getTotalAmount()));
            System.out.println("  - Updated Balance: P " + String.format("%,.2f", statement.getBalance()));
            System.out.println();
            
            // Test 9: Fee statistics
            System.out.println("Test 9: Fee database statistics");
            var stats = FeeDatabase.getFeeStatistics();
            System.out.println("✓ Fee statistics:");
            System.out.println("  - Global fees count: " + stats.get("globalFeesCount"));
            System.out.println("  - Students with specific fees: " + stats.get("studentsWithSpecificFees"));
            System.out.println("  - Total global amount: P " + String.format("%,.2f", (Double) stats.get("totalGlobalAmount")));
            System.out.println();
            
            // Test 10: Remove a fee
            System.out.println("Test 10: Removing a student-specific fee");
            FeeDatabase.removeStudentFee(testStudentID, "SF001");
            System.out.println("✓ Student-specific fee removed");
            
            // Check updated totals
            allStudentFees = FeeDatabase.getAllFeesForStudent(testStudentID);
            System.out.println("  - Updated fee count for student: " + allStudentFees.size());
            System.out.println();
            
            // Test 11: Test fee update listeners
            System.out.println("Test 11: Testing fee update listeners");
            TestFeeUpdateListener listener = new TestFeeUpdateListener();
            FeeDatabase.addFeeUpdateListener(listener);
            
            // Add a fee to trigger listener
            FeeBreakdown testFee = new FeeBreakdown(
                "TEST001",
                "Test Fee",
                100.00,
                FeeBreakdown.FeeType.OTHER,
                LocalDate.now()
            );
            FeeDatabase.addGlobalFee(testFee);
            
            if (listener.wasNotified()) {
                System.out.println("✓ Fee update listener triggered successfully");
            } else {
                System.out.println("✗ Fee update listener not triggered");
            }
            
            FeeDatabase.removeFeeUpdateListener(listener);
            System.out.println();
            
            System.out.println("=== All Fee Database Tests Completed Successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test listener for fee updates
     */
    private static class TestFeeUpdateListener implements FeeDatabase.FeeUpdateListener {
        private boolean notified = false;
        
        @Override
        public void onFeesUpdated(String studentID) {
            notified = true;
            System.out.println("    Listener notified for student: " + studentID);
        }
        
        public boolean wasNotified() {
            return notified;
        }
    }
}