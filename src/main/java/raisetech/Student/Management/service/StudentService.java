package raisetech.Student.Management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.data.StudentsCourses;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    //検索結果
    this.repository = repository;
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




public List<Student> searchStudentList(){

    return repository.search();
  }

  public StudentDetail searchStudent(String id){
   Student student =repository.searchStudent(id);
   List<StudentsCourses>studentsCourses=repository.searchStudentsCourses(student.getId());
   StudentDetail studentDetail=new StudentDetail();
   studentDetail.setStudent(student);
   studentDetail.setStudentsCourses(studentsCourses);
   return studentDetail;
  }

  public List<StudentsCourses> searchStudentsCourseList(){
    return repository.searchStudentsCoursesList();
  }
  @Transactional
  public void registerStudent(StudentDetail studentDetail){
  repository.registerStudent(studentDetail.getStudent());
  // TODO:コース情報登録も行う。
    for(StudentsCourses studentsCourses : studentDetail.getStudentsCourses()){
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      studentsCourses.setCourseStartAt(LocalDateTime.now());
      studentsCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));
      repository.registerStudentsCourses(studentsCourses);
    }

  }
  @Transactional
  public void updateStudent(StudentDetail studentDetail){
    for(StudentsCourses studentsCourses : studentDetail.getStudentsCourses()){
      studentsCourses.setStudentId(studentDetail.getStudent().getId());
      repository.updateStudentsCourses(studentsCourses);
    }

  }
}
