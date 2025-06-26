package raisetech.Student.Management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.data.StudentsCourses;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.repository.StudentRepository;

/**
 *　受講生情報を取り扱うサービスです。
 * 　受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;
  @Autowired
  public StudentService(StudentRepository repository,StudentConverter converter) {
    //検索結果
    this.repository = repository;
    this.converter = converter;
  }

  /**
   *
    * @return
   * // 年齢が30代の人を抽出
   *     public List<Student> searchStudentList() {
   *       return repository.search().stream()
   *           .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
   *           .collect(Collectors.toList());
   *     }
   *
   *   // Javaコース情報のみを抽出（StudentsCourses クラスも必要）
   *   public List<StudentsCourses> searchStudentsCourseList() {
   *     return repository.searchStudentsCourses().stream()
   *         .filter(course -> "Javaコース".equals(course.getCourseName()))
   *         .collect(Collectors.toList());
   *   }
   * }
   */


  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   * @return　受講生一覧（全件）
   */

public List<StudentDetail> searchStudentList(){
 List<Student> studentList =repository.search();
 List<StudentsCourses> studentCoursesList = repository.searchStudentsCoursesList();
    return converter.convertStudentDetails(studentList, studentCoursesList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   * @param id　受講生ID
   * @return　受講生
   */
  public StudentDetail searchStudent(String id){
   Student student =repository.searchStudent(id);
   List<StudentsCourses>studentsCourses=repository.searchStudentsCourses(student.getId());
   return  new StudentDetail(student,studentsCourses);
  }

  public List<StudentsCourses> searchStudentsCourseList(){
    return repository.searchStudentsCoursesList();
  }
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail){
  repository.registerStudent(studentDetail.getStudent());
  // TODO:コース情報登録も行う。
    for(StudentsCourses studentsCourses : studentDetail.getStudentsCourses()){
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      studentsCourses.setCourseStartAt(LocalDateTime.now());
      studentsCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentsCourses(studentsCourses);
    }
  return studentDetail;
  }
  @Transactional
  public void updateStudent(StudentDetail studentDetail){
    repository.updateStudent(studentDetail.getStudent());
    for(StudentsCourses studentsCourses : studentDetail.getStudentsCourses()){
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentsCourses(studentsCourses);
    }

  }
}
