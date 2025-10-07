package org.example.adventurexp.unit;

import org.example.adventurexp.dto.ReservationDTO;
import org.example.adventurexp.dto.UserDTO;
import org.example.adventurexp.model.Reservation;
import org.example.adventurexp.model.Role;
import org.example.adventurexp.model.User;
import org.example.adventurexp.repository.ReservationRepository;
import org.example.adventurexp.service.ReservationService;
import org.example.adventurexp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private UserService userService;

    @Mock
    private ReservationRepository reservationRepository;

    private List<Reservation> mockReservations;

    private List<ReservationDTO> mockReservationDTOList;

    private String formattedStartDate;

    private User mockUser;
    private UserDTO mockUserDTO;

    @BeforeEach
    void setup() {
        LocalDateTime startDate = LocalDateTime.of(2025, 10, 7, 14, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        formattedStartDate = startDate.format(formatter);

        mockUserDTO = new UserDTO(1, "bob", "Bob", "Dylan", Role.CUSTOMER, "bob@email.com", "01-01-2024");
        mockUser = new User(1, "bob", "Bob", "Dylan", Role.CUSTOMER, "bob@email.com", "hashed", LocalDateTime.now());

        mockReservations = new ArrayList<>();

        mockReservations.add(new Reservation(
                "Teambuilding",
                10,
                LocalDateTime.of(2025, 10, 7, 14, 0),
                true,
                new ArrayList<>()
        ));

        mockReservations.add(new Reservation(
                "Familiedag",
                5,
                LocalDateTime.of(2025, 11, 2, 10, 30),
                false,
                new ArrayList<>()
        ));

        mockReservations.add(new Reservation(
                "Firmajulefrokost",
                25,
                LocalDateTime.of(2025, 12, 14, 18, 0),
                true,
                new ArrayList<>()
        ));

        // Sæt bruger på hver reservation (da det er @ManyToOne)
        mockReservations.forEach(r -> r.setUser(mockUser));

        // --- DTO’er (samme data, men startDate som String) ---
        mockReservationDTOList = new ArrayList<>();

        mockReservationDTOList.add(new ReservationDTO(
                "Teambuilding",
                LocalDateTime.of(2025, 10, 7, 14, 0).format(formatter),
                10,
                true,
                new ArrayList<>()
        ));

        mockReservationDTOList.add(new ReservationDTO(
                "Familiedag",
                LocalDateTime.of(2025, 11, 2, 10, 30).format(formatter),
                5,
                false,
                new ArrayList<>()
        ));

        mockReservationDTOList.add(new ReservationDTO("Firmajulefrokost",
                LocalDateTime.of(2025, 12, 14, 18, 0).format(formatter),
                25,
                true,
                new ArrayList<>()
        ));
    }

    @Test
    void getReservationById_shouldReturnReservation_whenReservationIsValid(){

        // Arrange
        Reservation Familiedag = mockReservations.get(1);

        //Mock Behaviour
        when(reservationRepository.findById(2)).thenReturn(Optional.of(Familiedag));

        // Act
        Reservation result = reservationService.getReservationById(2);

        // Assert
        assertEquals(Familiedag,result);

        // Verify
        Mockito.verify(reservationRepository, Mockito.times(1)).findById(2);

    }
    @Test
    void makeReservation_saveReservation_whenReservationIsValid() {

        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedStartDate = LocalDateTime.of(2025, 12, 20, 18, 0).format(formatter);
        LocalDateTime startDate = LocalDateTime.of(2025, 12, 20, 18, 0);

        ReservationDTO expectedDTO = new ReservationDTO(
                "Julefrokost", formattedStartDate,
                20, true,
                new ArrayList<>()
        );

        Reservation savedEntity = new Reservation(
                "Julefrokost",20,startDate,true,new ArrayList<>()
        );

        // Mock repository til at retunere en reservation
        when(reservationRepository.save(any())).thenReturn(savedEntity);

        // ACT
        ReservationDTO saved = reservationService.makeReservation(expectedDTO, mockUserDTO);

        //Assert
        assertEquals(saved,expectedDTO);

        Mockito.verify(reservationRepository, Mockito.times(1)).findAll();
    }

    @Test
    void makeReservation_throwIllegalArgument_whenTimeIsTaken(){
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime startDate = LocalDateTime.of(2025, 10, 7, 14, 0);

        ReservationDTO reservationDTO1 = new ReservationDTO(
                "Julefrokost", startDate.format(formatter),
                20, true,
                new ArrayList<>()
        );
        Reservation existingReservation = new Reservation(
                "Firmaevent",
                15,
                startDate,
                false,
                new ArrayList<>()
        );

        // Mock repository til at sige "tiden er taget"
        when(reservationRepository.existsByStartDate(any(LocalDateTime.class))).thenReturn(true);


        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                reservationService.makeReservation(reservationDTO1,mockUserDTO));

assertEquals("The selected time slot is already booked.",exception.getMessage());
        Mockito.verify(reservationRepository, Mockito.times(1)).existsByStartDate(startDate);


    }
}

