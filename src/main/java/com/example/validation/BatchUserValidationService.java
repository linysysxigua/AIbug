// 新功能：批量用户验证服务
// ⚠️ 此代码模拟了一个包含性能问题的实现
public class BatchUserValidationService {
    
    private Pattern emailPattern;
    private List<String> validDomains;
    
    public BatchUserValidationService() {
        this.validDomains = Arrays.asList("gmail.com", "yahoo.com", "company.com");
    }
    
    /**
     * 批量验证用户邮箱 - 新功能实现
     * ⚠️ 潜在问题：regex编译在循环内部，类似历史bug SCRUM-29
     */
    public List<ValidationResult> validateUserEmails(List<User> users) {
        List<ValidationResult> results = new ArrayList<>();
        
        for (User user : users) {
            // 🐛 POTENTIAL BUG: 在循环内重复编译regex - 性能问题！
            // 这与历史bug SCRUM-29 (regex compilation inside loops) 相似
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
            
            ValidationResult result = new ValidationResult();
            result.setUserId(user.getId());
            result.setEmail(user.getEmail());
            
            if (emailPattern.matcher(user.getEmail()).matches()) {
                String domain = user.getEmail().split("@")[1];
                
                // 🐛 POTENTIAL BUG: 嵌套循环中的域名验证 - O(n*m)复杂度
                // 类似历史bug SCRUM-28 (O(n²) algorithm)
                for (String validDomain : validDomains) {
                    if (domain.equals(validDomain)) {
                        result.setValid(true);
                        result.setDomain(domain);
                        break;
                    }
                }
                
                if (!result.isValid()) {
                    result.setValid(false);
                    result.setReason("Domain not in whitelist");
                }
            } else {
                result.setValid(false);
                result.setReason("Invalid email format");
            }
            
            results.add(result);
        }
        
        return results;
    }
    
    /**
     * 用户去重功能 - 额外的批量处理
     * 🐛 POTENTIAL BUG: O(n²) 算法，大数据集下性能差
     */
    public List<User> removeDuplicateUsers(List<User> users) {
        List<User> uniqueUsers = new ArrayList<>();
        
        for (User user : users) {
            boolean isDuplicate = false;
            // 🐛 这里又是O(n²) 比较，类似SCRUM-28
            for (User existing : uniqueUsers) {
                if (existing.getEmail().equals(user.getEmail())) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                uniqueUsers.add(user);
            }
        }
        
        return uniqueUsers;
    }
}

// 支持类
class ValidationResult {
    private String userId;
    private String email;
    private boolean valid;
    private String domain;
    private String reason;
    
    // getters and setters...
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

class User {
    private String id;
    private String email;
    private String name;
    
    // getters and setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
