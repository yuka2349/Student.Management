package raisetech.Student.Management.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.Student.Management.data.StudentCourse;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST　APIとしてうけつけるControllerです。
 */
@RestController
public class StudentController {

  private StudentService service;



  @Autowired
  public StudentController(StudentService service) {
    this.service = service;

  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行わないものになります。
   * @return　受講生一覧（全件）
   */

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    //リクエストの処理
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   * @param id　受講生ID
   * @return　受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable String id) {
    return service.searchStudent(id);
  }


  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentsCourseList(Arrays.asList(new StudentCourse()));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  /**
   * 受講生情報の更新を行います。
   * キャンセルフラグの更新もここで行います（論理削除）
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */

  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 受講生詳細の登録を行います。
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail){
    StudentDetail resuponseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(resuponseStudentDetail);
    }

  }



