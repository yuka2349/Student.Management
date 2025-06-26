package raisetech.Student.Management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.data.StudentCourse;
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
   * 受講生詳細の一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   * @return　受講生一覧（全件）
   */

public List<StudentDetail> searchStudentList(){
 List<Student> studentList =repository.search();
 List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   * @param id　受講生ID
   * @return　受講生
   */
  public StudentDetail searchStudent(String id){
   Student student =repository.searchStudent(id);
   List<StudentCourse>studentCourse=repository.searchStudentCourse(student.getId());
   return  new StudentDetail(student,studentCourse);
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し受講生コース情報には受講生情報を紐づける値や日付情報(コース開始日、コース終了日）を設定します。
   * @param studentDetail
   * @return　登録情報を付与した受講生詳細
   */

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail){
    Student student = studentDetail.getStudent();

    repository.registerStudent(student);
  // TODO:コース情報登録も行う。
    studentDetail.getStudentsCourseList().forEach(studentsCourses -> {
      initStudentsCourse(studentsCourses, student);
      repository.registerStudentCourse(studentsCourses);
    });
  return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   * @param studentsCourses　受講生コース情報
   * @param student　受講生
   */
  private void initStudentsCourse(StudentCourse studentsCourses, Student student) {
    studentsCourses.setStudentId(student.getId());
    LocalDateTime now = LocalDateTime.now();
    studentsCourses.setCourseStartAt(now);
    studentsCourses.setCourseEndAt(now.plusYears(1));
  }

  /**
   * 受講生情報の更新を行います。
   * 受講生の情報と受講生コース情報をそれぞれ実行します。
   * @param studentDetail　受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail){
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentsCourseList()
        .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
    }

  }

