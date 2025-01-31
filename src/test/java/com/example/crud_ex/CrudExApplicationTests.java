package com.example.crud_ex;

import controller.*;
import entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class CrudExApplicationTests {

	@Autowired
	private StudentController studentController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void studentControllerLoads() {
		assertThat(studentController).isNotNull();
	}

	private Student getStudentFromId(Long id) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/student/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		try {
			String userJSON = result.getResponse().getContentAsString();
			Student student = objectMapper.readValue(userJSON, Student.class);

			assertThat(student).isNotNull();
			assertThat(student.getId()).isNotNull();
			return student;

		} catch (Exception e) {
			return null;
		}
	}

	private Student createAStudent() throws Exception {
		Student student = new Student();
		student.setIsWorking(true);
		student.setName("Giulia");
		student.setSurname("Ciao");

		return createAStudent(student);
	}

	private Student createAStudent(Student student) throws Exception {
		MvcResult result = createAStudentRequest(student);
		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isNotNull();

		return studentFromResponse;
	}

	private MvcResult createAStudentRequest() throws Exception {
		Student student = new Student();
		student.setIsWorking(true);
		student.setName("Giulia");
		student.setSurname("Ciao");

		return createAStudentRequest(student);
	}

	private MvcResult createAStudentRequest(Student student) throws Exception {
		if (student == null) return null;
		String userJSON = objectMapper.writeValueAsString(student);

		return this.mockMvc.perform(post("/student/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

	}

	@Test
	void createAStudentTest() throws Exception {

		Student studentFromResponse = createAStudent();
	}

	@Test
	void readStudentsList() throws Exception {
		createAStudentRequest();

		MvcResult result = this.mockMvc.perform(get("/student/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentsFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		System.out.println("Students in database are: " + studentsFromResponse.size());
		assertThat(studentsFromResponse.size()).isNotZero();
	}

	@Test
	void readSingleStudent() throws Exception {
		Student student = createAStudent();
		Student studentFromResponse = getStudentFromId(student.getId());
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());

	}

	@Test
	void updateStudent() throws Exception {
		Student student = createAStudent();

		String newName = "Salvo";
		student.setName(newName);
		String userJSON = objectMapper.writeValueAsString(student);


		MvcResult resultOne = this.mockMvc.perform(put("/student/set/" + student.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		Student studentFromResponse = objectMapper.readValue(resultOne.getResponse().getContentAsString(), Student.class);


		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getName()).isEqualTo(newName);


		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.getName()).isEqualTo(newName);
	}

	@Test
	void deleteStudent() throws Exception {
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		this.mockMvc.perform(delete("/student/" + student.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNull();
	}

	@Test
	void workingStudent() throws Exception {
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();


		MvcResult result = this.mockMvc.perform(put("/student/setIsWorking/" + student.getId() + "?working=true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getIsWorking()).isEqualTo(true);

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNotNull();
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.getIsWorking()).isEqualTo(true);
	}


}