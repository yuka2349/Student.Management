package raisetech.Student.Management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.data.StudentsCourses;

/**
 *  受講生情報を扱うリポジトリ
 *
 *  全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
@Repository
public interface StudentRepository {

  /**
   * 全件検索
   * @return 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students" )
  List<Student> search();

  @Select("SELECT * FROM students WHERE id=#{id}" )
  Student searchStudent(String id);

  @Select("SELECT * FROM students_courses" )
  List<StudentsCourses> searchStudentsCoursesList();

  @Select("SELECT * FROM students_courses WHERE student_id =#{studentId}" )
  List<StudentsCourses> searchStudentsCourses(String studentId);

  @Insert("INSERT INTO students(name,kana_name,nickname,email,area,age,sex,remark,isDeleted)"
      + "VALUES(#{name},#{kanaName},#{nickName},#{email},#{area},#{age},#{sex},#{remark},false)")
  @Options(useGeneratedKeys = true,keyProperty = "id")
  void registerStudent(Student student);

  @Insert("INSERT INTO students_courses(student_id,course_name,course_start_at,course_end_at)"
      + "VALUES(#{studentId},#{courseName},#{courseStartAt},#{courseEndAt})")
  void registerStudentsCourses(StudentsCourses studentsCourses);

  @Update("UPDATE students SET name=#{name},kana_name=#{kanaName},nickName=#{nickName},"
      + "email=#{email},area=#{area},age=#{age},sex=#{sex},remark=#{remark},isDeleted=#{isDeleted} WHERE id=#{id}")

  void updateStudent(Student student);

  @Update("UPDATE students_courses SET course_name=#{courseName} WHERE id =#{id}")
  void updateStudentsCourses(StudentsCourses studentsCourses);
}
