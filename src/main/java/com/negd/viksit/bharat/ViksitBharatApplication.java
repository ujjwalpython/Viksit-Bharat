package com.negd.viksit.bharat;

import com.negd.viksit.bharat.model.Role;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Department;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.model.master.TargetIndicator;
import com.negd.viksit.bharat.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class ViksitBharatApplication extends SpringBootServletInitializer {

    private final UserRepository userRepository;

    public ViksitBharatApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ViksitBharatApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ViksitBharatApplication.class);
    }

    @Bean
    CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder, UserRepository userRepository,
                                        MinistryRepository ministryRepository, DepartmentRepository departmentRepository, TargetIndicatorRepository targetIndicatorRepository, RoleRepository roleRepository) {
        return (args) -> {

            List<Role> roleList = List.of();
            if (roleRepository.count() == 0) {
                List<Role> roles = List.of(
                        Role.builder().name("MINISTRY_ADMIN").description("Ministry admins").build(),
                        Role.builder().name("DEPT_ADMIN").description("Department admins").build(),
                        Role.builder().name("PMO_USER").description("PMO user").build(),
                        Role.builder().name("CBSEC_USER").description("Cabinet Secretariat user").build(),
                        Role.builder().name("SUPER_ADMIN").description("Super admin").build()
                );
                roleList = roleRepository.saveAll(roles);
            }
            // ------------------ SEED MINISTRIES ------------------
            List<Ministry> ministries = List.of();
            if (ministryRepository.findAll().isEmpty()) {
                ministries = List.of(
                        Ministry.builder().code("MOA").name("Ministry of Agriculture and Farmers Welfare")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOAYUSH").name("Ministry of AYUSH").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOCF").name("Ministry of Chemicals and Fertilizers").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOCA").name("Ministry of Civil Aviation").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOCOAL").name("Ministry of Coal").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOCI").name("Ministry of Commerce and Industry").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOCAFPD")
                                .name("Ministry of Consumer Affairs, Food and Public Distribution").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOCOM").name("Ministry of Communications").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MCA").name("Ministry of Corporate Affairs").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOCUL").name("Ministry of Culture").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOD").name("Ministry of Defence").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MDONER").name("Ministry of Development of North Eastern Region")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOES").name("Ministry of Earth Sciences").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MEITY").name("Ministry of Electronics and Information Technology")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOEFCC").name("Ministry of Environment, Forest and Climate Change")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MEA").name("Ministry of External Affairs").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOF").name("Ministry of Finance").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOFAHD").name("Ministry of Fisheries, Animal Husbandry and Dairying")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOFPI").name("Ministry of Food Processing Industries").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOHFW").name("Ministry of Health & Family Welfare").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOHI").name("Ministry of Heavy Industries").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MHA").name("Ministry of Home Affairs").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOHUA").name("Ministry of Housing and Urban Affairs").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOE").name("Ministry of Education").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MIB").name("Ministry of Information and Broadcasting").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOJS").name("Ministry of Jal Shakti").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOLE").name("Ministry of Labour & Employment").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOLJ").name("Ministry of Law and Justice").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MSME").name("Ministry of Micro, Small and Medium Enterprises")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOM").name("Ministry of Mines").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOMA").name("Ministry of Minority Affairs").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MNRE").name("Ministry of New and Renewable Energy").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOPR").name("Ministry of Panchayati Raj").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MPA").name("Ministry of Parliamentary Affairs").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOPPGP").name("Ministry of Personnel, Public Grievances and Pensions")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOPNG").name("Ministry of Petroleum and Natural Gas").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOPWR").name("Ministry of Power").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOR").name("Ministry of Railways").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MORTH").name("Ministry of Road Transport and Highways").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MORD").name("Ministry of Rural Development").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MST").name("Ministry of Science and Technology").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MSDE").name("Ministry of Skill Development and Entrepreneurship")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOSJE").name("Ministry of Social Justice and Empowerment")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOSPI").name("Ministry of Statistics and Programme Implementation")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOS").name("Ministry of Steel").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOT").name("Ministry of Textiles").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOTOUR").name("Ministry of Tourism").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("MOTA").name("Ministry of Tribal Affairs").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOWCD").name("Ministry of Women and Child Development").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOYAS").name("Ministry of Youth Affairs and Sports").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOCOOP").name("Ministry of Cooperation").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("MOPSW").name("Ministry of Ports, Shipping and Waterways")
                                .isActive(true).isDeleted(false).build(),
                        Ministry.builder().code("MOPLAN").name("Ministry of Planning").isActive(true).isDeleted(false)
                                .build(),
                        Ministry.builder().code("DEPTSPC").name("Department of Space").isActive(true)
                                .isDeleted(false).build(),
                        Ministry.builder().code("DAE").name("Department of Atomic Energy").isActive(true)
                                .isDeleted(false).build());
                ministries = ministryRepository.saveAll(ministries);
            }

            AtomicReference<List<Department>> departments = new AtomicReference<>(new ArrayList<>());
            // ------------------ SEED DEPARTMENTS ------------------
            if (departmentRepository.findAll().isEmpty()) {
                Map<String, List<Department>> deptMap = Map.ofEntries(
                        Map.entry("MOA",
                                List.of(Department.builder().code("DARE")
                                                .name("Department of Agricultural Research and Education").isActive(true)
                                                .isDeleted(false).build(),
                                        Department.builder().code("DoAFW")
                                                .name("Department of Agriculture and Farmers Welfare").isActive(true)
                                                .isDeleted(false).build())),
                        Map.entry("MOCF", List.of(
                                Department.builder().code("DoCP").name("Department of Chemicals and Petrochemicals")
                                        .isActive(true).isDeleted(false).build(),
                                Department.builder().code("DoFZ").name("Department of Fertilizers").isActive(true)
                                        .isDeleted(false).build(),
                                Department.builder().code("DoPs").name("Department of Pharmaceuticals").isActive(true)
                                        .isDeleted(false).build())),
                        Map.entry("MOCI",
                                List.of(Department.builder().code("DOC").name("Department of Commerce").isActive(true)
                                                .isDeleted(false).build(),
                                        Department.builder().code("DPIIT")
                                                .name("Department for Promotion of Industry and Internal Trade")
                                                .isActive(true).isDeleted(false).build())),
                        Map.entry("MOCAFPD", List.of(
                                Department.builder().code("DoCA").name("Department of Consumer Affairs").isActive(true)
                                        .isDeleted(false).build(),
                                Department.builder().code("DFPD").name("Department of Food and Public Distribution")
                                        .isActive(true).isDeleted(false).build())),
                        Map.entry("MOCOM",
                                List.of(Department.builder().code("DoP").name("Department of Posts").isActive(true)
                                                .isDeleted(false).build(),
                                        Department.builder().code("DoT").name("Department of Telecommunications")
                                                .isActive(true).isDeleted(false).build())),
                        Map.entry("MOD", List.of(
                                Department.builder().code("DoD").name("Department of Defence").isActive(true)
                                        .isDeleted(false).build(),
                                Department.builder().code("DDP").name("Department of Defence Production").isActive(true)
                                        .isDeleted(false).build(),
                                Department.builder().code("DRDO").name("Department of Defence Research and Development")
                                        .isActive(true).isDeleted(false).build(),
                                Department.builder().code("DESW").name("Department of Ex-Servicemen Welfare")
                                        .isActive(true).isDeleted(false).build(),
                                Department.builder().code("DMA").name("Department of Military Affairs").isActive(true)
                                        .isDeleted(false).build())),
                        Map.entry("MOF",
                                List.of(Department.builder().code("DEA").name("Department of Economic Affairs")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DOE").name("Department of Expenditure")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DOFS").name("Department of Financial Services")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DIPAM")
                                                .name("Department of Investment and Public Asset Management")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DPE").name("Department of Public Enterprises")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DOR").name("Department of Revenue").isActive(true)
                                                .isDeleted(false).build())),
                        Map.entry("MOFAHD", List.of(
                                Department.builder().code("DoAHD").name("Department of Animal Husbandry and Dairying")
                                        .isActive(true).isDeleted(false).build(),
                                Department.builder().code("DoF").name("Department of Fisheries").isActive(true)
                                        .isDeleted(false).build())),
                        Map.entry("MOHFW", List.of(
                                Department.builder().code("DHR").name("Department of Health Research").isActive(true)
                                        .isDeleted(false).build(),
                                Department.builder().code("DoHFW").name("Department of Health and Family Welfare")
                                        .isActive(true).isDeleted(false).build())),
                        Map.entry("MHA",
                                List.of(Department.builder().code("DoH").name("Department of Home").isActive(true)
                                                .isDeleted(false).build(),
                                        Department.builder().code("DoOL").name("Department of Official Language")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DoBM").name("Department of Border Management")
                                                .isActive(true).isDeleted(false).build())),
                        Map.entry("MOE", List.of(
                                Department.builder().code("DoHE").name("Department of Higher Education").isActive(true)
                                        .isDeleted(false).build(),
                                Department.builder().code("DSEL").name("Department of School Education and Literacy")
                                        .isActive(true).isDeleted(false).build())),
                        Map.entry("MOJS", List.of(
                                Department.builder().code("DDWS").name("Department of Drinking Water and Sanitation")
                                        .isActive(true).isDeleted(false).build(),
                                Department.builder().code("DoWRGR")
                                        .name("Department of Water Resources, River Development and Ganga Rejuvenation")
                                        .isActive(true).isDeleted(false).build())),
                        Map.entry("MOLJ",
                                List.of(Department.builder().code("DoJ").name("Department of Justice").isActive(true)
                                                .isDeleted(false).build(),
                                        Department.builder().code("DoLA").name("Department of Legal Affairs")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("LD").name("Legislative Department").isActive(true)
                                                .isDeleted(false).build())),
                        Map.entry("MOPPGP", List.of(
                                Department.builder().code("DARPG")
                                        .name("Department of Administrative Reforms and Public Grievances")
                                        .isActive(true).isDeleted(false).build(),
                                Department.builder().code("DoPPW").name("Department of Pension & Pensioner's Welfare")
                                        .isActive(true).isDeleted(false).build(),
                                Department.builder().code("DoPT").name("Department of Personnel and Training")
                                        .isActive(true).isDeleted(false).build())),
                        Map.entry("MORD",
                                List.of(Department.builder().code("DLR").name("Department of Land Resources")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DoRD").name("Department of Rural Development")
                                                .isActive(true).isDeleted(false).build())),
                        Map.entry("MST",
                                List.of(Department.builder().code("DBT").name("Department of Bio-Technology")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DST").name("Department of Science and Technology")
                                                .isActive(true).isDeleted(false).build(),
                                        Department.builder().code("DSIR")
                                                .name("Department of Scientific and Industrial Research").isActive(true)
                                                .isDeleted(false).build())),
                        Map.entry("MOSJE",
                                List.of(Department.builder().code("DEPWD")
                                                .name("Department of Empowerment of Persons with Disabilities").isActive(true)
                                                .isDeleted(false).build(),
                                        Department.builder().code("DoSJE")
                                                .name("Department of Social Justice and Empowerment").isActive(true)
                                                .isDeleted(false).build())),
                        Map.entry("MOYAS",
                                List.of(Department.builder().code("DoS").name("Department of Sports").isActive(true)
                                                .isDeleted(false).build(),
                                        Department.builder().code("DoYA").name("Department of Youth Affairs")
                                                .isActive(true).isDeleted(false).build())));

                deptMap.forEach((ministryCode, depts) -> {
                    Ministry ministry = ministryRepository.findByCode(ministryCode)
                            .orElseThrow(() -> new EntityNotFoundException("Ministry not found: " + ministryCode));
                    depts.forEach(d -> d.setMinistry(ministry));
                    departments.set(departmentRepository.saveAll(depts));
                });
            }

            if (userRepository.count() == 0) {
                List<User> users = List.of(
                        User.builder()
                                .firstName("Ministry")
                                .lastName("Admin")
                                .email("ministry.admin@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .ministry(ministries.stream().filter(ministry -> ministry.getDepartments()==null).findFirst().get())
                                .roles(Set.of(roleList.get(0)))
                                .build(),

                        User.builder()
                                .firstName("Department")
                                .lastName("Admin")
                                .email("dept.admin@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .department(departments.get().get(0))
                                .ministry(departments.get().get(0).getMinistry())
                                .roles(Set.of(roleList.get(1)))
                                .build(),

                        User.builder()
                                .firstName("PMO")
                                .lastName("User")
                                .email("pmo.user@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .roles(Set.of(roleList.get(2)))
                                .build(),

                        User.builder()
                                .firstName("Cabsec")
                                .lastName("User")
                                .email("cabsec.user@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .roles(Set.of(roleList.get(3)))
                                .build(),

                        User.builder()
                                .firstName("Super")
                                .lastName("Admin")
                                .email("super.admin@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .roles(Set.of(roleList.get(4)))
                                .build()
                );

                userRepository.saveAll(users);
            }
            if (targetIndicatorRepository.findAll().isEmpty()) {
                List<TargetIndicator> indicators = List.of(
                        TargetIndicator.builder().code("GDP").name("GDP Growth Rate").isActive(true).isDeleted(false)
                                .build(),
                        TargetIndicator.builder().code("PCI").name("Per Capita Income").isActive(true).isDeleted(false)
                                .build(),
                        TargetIndicator.builder().code("DIGI").name("Digital Literacy Rate").isActive(true)
                                .isDeleted(false).build(),
                        TargetIndicator.builder().code("RENEW").name("Renewable Energy Capacity").isActive(true)
                                .isDeleted(false).build());
                targetIndicatorRepository.saveAll(indicators);
            }
        };

    }
}
