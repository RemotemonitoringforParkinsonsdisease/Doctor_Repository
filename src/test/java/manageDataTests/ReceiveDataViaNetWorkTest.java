package manageDataTests;

import POJOs.*;
import manageData.ReceiveDataViaNetwork;

import java.io.DataInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import POJOs.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceiveDataViaNetworkTest {

    private DataInputStream dis;
    private ReceiveDataViaNetwork receiver;

    @BeforeEach
    void setUp() {
        dis = mock(DataInputStream.class);
        receiver = new ReceiveDataViaNetwork(dis);
    }

    @Test
    void testReceiveString() throws Exception {
        when(dis.readUTF()).thenReturn("hola");

        String result = receiver.receiveString();

        assertEquals("hola", result);
        verify(dis).readUTF();
    }

    @Test
    void testReceiveUser() throws Exception {
        when(dis.readInt()).thenReturn(10);
        when(dis.readUTF()).thenReturn("test@example.com");

        User u = receiver.receiveUser();

        assertEquals(10, u.getUserId());
        assertEquals("test@example.com", u.getEmail());
    }

    @Test
    void testReceivePatient() throws Exception {
        when(dis.readInt()).thenReturn(5);
        when(dis.readUTF()).thenReturn("2000-01-01", "John Doe");

        Patient p = receiver.recievePatient();

        assertEquals(5, p.getPatientId());
        assertEquals("John Doe", p.getFullName());
        assertEquals(LocalDate.of(2000, 1, 1), p.getDob());
    }

    @Test
    void testReceivePatients() throws Exception {
        // se leen N pacientes
        when(dis.readInt()).thenReturn(2); // 2 pacientes

        // Paciente 1
        when(dis.readInt()).thenReturn(1, 2); // patientId1, patientId2
        when(dis.readUTF()).thenReturn(
                "1990-05-10", "Paciente1",
                "1980-02-15", "Paciente2"
        );

        List<Patient> patients = receiver.receivePatients();

        assertEquals(2, patients.size());
        assertEquals(1, patients.get(0).getPatientId());
        assertEquals("Paciente1", patients.get(0).getFullName());
        assertEquals(2, patients.get(1).getPatientId());
        assertEquals("Paciente2", patients.get(1).getFullName());
    }

    @Test
    void testReceiveSymptoms() throws Exception {
        when(dis.readUTF()).thenReturn("FEVER, COUGH");

        List<Symptoms> symptoms = receiver.receiveSymptoms();

        assertEquals(2, symptoms.size());
        assertEquals(Symptoms.FEVER, symptoms.get(0));
        assertEquals(Symptoms.COUGH, symptoms.get(1));
    }

    @Test
    void testReceiveCSVFile() throws Exception {

        // Simulamos nombre y tamaño
        when(dis.readUTF()).thenReturn("test.csv");
        when(dis.readLong()).thenReturn(10L);

        // Simulamos contenido de los bytes
        when(dis.read(any(byte[].class), anyInt(), anyInt()))
                .then(invocation -> {
                    byte[] buffer = invocation.getArgument(0);
                    buffer[0] = 'a';
                    buffer[1] = 'b';
                    buffer[2] = 'c';
                    buffer[3] = 'd';
                    buffer[4] = 'e';
                    buffer[5] = 'f';
                    buffer[6] = 'g';
                    buffer[7] = 'h';
                    buffer[8] = 'i';
                    buffer[9] = 'j';
                    return 10;
                })
                .thenReturn(-1);

        String path = receiver.receiveCSVFile();

        assertTrue(Files.exists(Paths.get(path)));
        assertEquals(10, Files.size(Paths.get(path)));

        // cleanup
        Files.deleteIfExists(Paths.get(path));
    }

    @Test
    void testReceiveReport() throws Exception {

        when(dis.readInt()).thenReturn(1, 100); // reportId, patientId
        when(dis.readUTF()).thenReturn("2023-10-10"); // fecha

        // CSV
        when(dis.readUTF()).thenReturn("file.csv"); // nombre
        when(dis.readLong()).thenReturn(0L); // file size

        // Symptoms
        when(dis.readUTF()).thenReturn("FEVER, COUGH");

        // patient + doctor observations
        when(dis.readUTF()).thenReturn("Estoy mal", "Reposo");

        Report report = receiver.receiveReport();

        assertEquals(1, report.getReportId());
        assertEquals(100, report.getPatientId());
        assertEquals(LocalDate.of(2023, 10, 10), report.getReportDate());
        assertEquals(2, report.getSymptoms().size());
        assertEquals("Estoy mal", report.getPatientObservation());
        assertEquals("Reposo", report.getDoctorObservation());
    }

    @Test
    void testReceiveDoctor() throws Exception {

        // doctor basic fields
        when(dis.readInt()).thenReturn(10, 20); // userId, doctorId
        when(dis.readUTF()).thenReturn("Dr Test", "clave123", "1980-01-01");

        // patients list
        when(dis.readInt()).thenReturn(1); // number of patients
        when(dis.readInt()).thenReturn(99); // id patient
        when(dis.readUTF()).thenReturn("2000-05-05", "PacientePrueba");

        Doctor doctor = receiver.receiveDoctor();

        assertEquals(10, doctor.getUserId());
        assertEquals(20, doctor.getDoctorId());
        assertEquals("Dr Test", doctor.getFullName());
        assertEquals(1, doctor.getPatients().size());
        assertEquals(99, doctor.getPatients().get(0).getPatientId());
    }
}


