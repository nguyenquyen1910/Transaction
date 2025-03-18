src/main/java/com/mongodb/: Đây là nơi chứa mã nguồn chính, với com.mongodb là groupId

config/: Chứa các lớp cấu hình, như kết nối MongoDB.

model/: Chứa các lớp POJO (Plain Old Java Object) đại diện cho dữ liệu

repository/: Chứa các lớp truy cập dữ liệu, tương tác trực tiếp với MongoDB (DAO - Data Access Object)

service/: Chứa logic nghiệp vụ, bao gồm các giao tác tập trung và phân tán.

resources/: Chứa file cấu hình (ví dụ: application.properties) để lưu thông tin như URI MongoDB.

src/test/: Chứa các bài kiểm tra (unit test) để đảm bảo chất lượng code.

pom.xml: File cấu hình Maven (đã cung cấp trước đó).


# Giao dịch tập trung
"Giao dịch tập trung xảy ra trong một replica set duy nhất của MongoDB.
Trong trường hợp này, chỉ thao tác trên một cơ sở dữ liệu duy nhất, và
tất cả các hoạt động của giao dịch (insert, update, delete) đều diễn ra trong phạm vi đó."


# Giao dịch phân tán
"Giao dịch phân tán xảy ra khi thực hiện các thao tác trên nhiều shard
(một nhóm các node độc lập trong MongoDB) hoặc nhiều cơ sở dữ liệu khác nhau.
Đây là trường hợp khi một giao dịch bao gồm việc thao tác trên nhiều collection, database, hoặc shard."