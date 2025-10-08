package com.negd.viksit.bharat;

import com.negd.viksit.bharat.model.Role;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Department;
import com.negd.viksit.bharat.model.master.GovernmentEntity;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                                        MinistryRepository ministryRepository, DepartmentRepository departmentRepository, TargetIndicatorRepository targetIndicatorRepository, RoleRepository roleRepository,
                                        GovernmentEntityRepository governmentEntityRepository) {
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
            List<GovernmentEntity> allGovernmentEntities = new ArrayList<>();

            if (governmentEntityRepository.count() == 0) {

                // 1️⃣ Define ministries
                List<GovernmentEntity> ministries = List.of(
                        GovernmentEntity.builder().code("MOA").name("Ministry of Agriculture and Farmers Welfare").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOAYUSH").name("Ministry of AYUSH").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOCF").name("Ministry of Chemicals and Fertilizers").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOCA").name("Ministry of Civil Aviation").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOCOAL").name("Ministry of Coal").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOCI").name("Ministry of Commerce and Industry").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOCAFPD").name("Ministry of Consumer Affairs, Food and Public Distribution").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOCOM").name("Ministry of Communications").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MCA").name("Ministry of Corporate Affairs").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOCUL").name("Ministry of Culture").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOD").name("Ministry of Defence").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MDONER").name("Ministry of Development of North Eastern Region").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOES").name("Ministry of Earth Sciences").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MEITY").name("Ministry of Electronics and Information Technology").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOEFCC").name("Ministry of Environment, Forest and Climate Change").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MEA").name("Ministry of External Affairs").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOF").name("Ministry of Finance").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOFAHD").name("Ministry of Fisheries, Animal Husbandry and Dairying").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOFPI").name("Ministry of Food Processing Industries").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOHFW").name("Ministry of Health & Family Welfare").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOHI").name("Ministry of Heavy Industries").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MHA").name("Ministry of Home Affairs").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOHUA").name("Ministry of Housing and Urban Affairs").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOE").name("Ministry of Education").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MIB").name("Ministry of Information and Broadcasting").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOJS").name("Ministry of Jal Shakti").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOLE").name("Ministry of Labour & Employment").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOLJ").name("Ministry of Law and Justice").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MSME").name("Ministry of Micro, Small and Medium Enterprises").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOM").name("Ministry of Mines").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOMA").name("Ministry of Minority Affairs").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MNRE").name("Ministry of New and Renewable Energy").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOPR").name("Ministry of Panchayati Raj").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MPA").name("Ministry of Parliamentary Affairs").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOPPGP").name("Ministry of Personnel, Public Grievances and Pensions").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOPNG").name("Ministry of Petroleum and Natural Gas").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOPWR").name("Ministry of Power").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOR").name("Ministry of Railways").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MORTH").name("Ministry of Road Transport and Highways").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MORD").name("Ministry of Rural Development").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MST").name("Ministry of Science and Technology").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MSDE").name("Ministry of Skill Development and Entrepreneurship").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOSJE").name("Ministry of Social Justice and Empowerment").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOSPI").name("Ministry of Statistics and Programme Implementation").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOS").name("Ministry of Steel").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOT").name("Ministry of Textiles").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOTOUR").name("Ministry of Tourism").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOTA").name("Ministry of Tribal Affairs").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOWCD").name("Ministry of Women and Child Development").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOYAS").name("Ministry of Youth Affairs and Sports").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOCOOP").name("Ministry of Cooperation").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOPSW").name("Ministry of Ports, Shipping and Waterways").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("MOPLAN").name("Ministry of Planning").type("MINISTRY").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("DEPTSPC").name("Department of Space").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                        GovernmentEntity.builder().code("DAE").name("Department of Atomic Energy").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                );

                Map<String, List<GovernmentEntity>> deptMap = Map.ofEntries(
                        Map.entry("MOA", List.of(
                                GovernmentEntity.builder().code("DARE").name("Department of Agricultural Research and Education").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOAFW").name("Department of Agriculture and Farmers Welfare").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOCF", List.of(
                                GovernmentEntity.builder().code("DOCP").name("Department of Chemicals and Petrochemicals").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOFZ").name("Department of Fertilizers").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOPS").name("Department of Pharmaceuticals").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOCI", List.of(
                                GovernmentEntity.builder().code("DOC").name("Department of Commerce").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DPIIT").name("Department for Promotion of Industry and Internal Trade").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOCAFPD", List.of(
                                GovernmentEntity.builder().code("DOCA").name("Department of Consumer Affairs").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DFPD").name("Department of Food and Public Distribution").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOCOM", List.of(
                                GovernmentEntity.builder().code("DOP").name("Department of Posts").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOT").name("Department of Telecommunications").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOD", List.of(
                                GovernmentEntity.builder().code("DOD").name("Department of Defence").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DDP").name("Department of Defence Production").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DRDO").name("Department of Defence Research and Development").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DESW").name("Department of Ex-Servicemen Welfare").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DMA").name("Department of Military Affairs").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOF", List.of(
                                GovernmentEntity.builder().code("DEA").name("Department of Economic Affairs").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOE").name("Department of Expenditure").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOFS").name("Department of Financial Services").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DIPAM").name("Department of Investment and Public Asset Management").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DPE").name("Department of Public Enterprises").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOR").name("Department of Revenue").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOFAHD", List.of(
                                GovernmentEntity.builder().code("DOAHD").name("Department of Animal Husbandry and Dairying").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOF").name("Department of Fisheries").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOHFW", List.of(
                                GovernmentEntity.builder().code("DHR").name("Department of Health Research").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOHFW").name("Department of Health and Family Welfare").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MHA", List.of(
                                GovernmentEntity.builder().code("DOH").name("Department of Home").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOOL").name("Department of Official Language").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOBM").name("Department of Border Management").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOE", List.of(
                                GovernmentEntity.builder().code("DOHE").name("Department of Higher Education").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DSEL").name("Department of School Education and Literacy").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOJS", List.of(
                                GovernmentEntity.builder().code("DDWS").name("Department of Drinking Water and Sanitation").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOWRGR").name("Department of Water Resources, River Development and Ganga Rejuvenation").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOLJ", List.of(
                                GovernmentEntity.builder().code("DOJ").name("Department of Justice").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOLA").name("Department of Legal Affairs").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("LD").name("Legislative Department").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOPPGP", List.of(
                                GovernmentEntity.builder().code("DARPG").name("Department of Administrative Reforms and Public Grievances").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOPPW").name("Department of Pension & Pensioner's Welfare").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOPT").name("Department of Personnel and Training").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MORD", List.of(
                                GovernmentEntity.builder().code("DLR").name("Department of Land Resources").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DORD").name("Department of Rural Development").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MST", List.of(
                                GovernmentEntity.builder().code("DBT").name("Department of Bio-Technology").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DST").name("Department of Science and Technology").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DSIR").name("Department of Scientific and Industrial Research").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOSJE", List.of(
                                GovernmentEntity.builder().code("DEPWD").name("Department of Empowerment of Persons with Disabilities").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOSJE").name("Department of Social Justice and Empowerment").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        )),
                        Map.entry("MOYAS", List.of(
                                GovernmentEntity.builder().code("DOS").name("Department of Sports").type("DEPARTMENT").isActive(true).isDeleted(false).build(),
                                GovernmentEntity.builder().code("DOYA").name("Department of Youth Affairs").type("DEPARTMENT").isActive(true).isDeleted(false).build()
                        ))
                );

                List<GovernmentEntity> entitiesToSave = new ArrayList<>(ministries);
                deptMap.forEach((ministryCode, depts) -> {
                    GovernmentEntity parent = ministries.stream()
                            .filter(m -> m.getCode().equals(ministryCode))
                            .findFirst()
                            .orElseThrow(() -> new EntityNotFoundException("Ministry not found: " + ministryCode));
                    depts.forEach(d -> {d.setParent(parent);
                        d.setName(parent.getName()+ " / " + d.getName());
                    });
                    entitiesToSave.addAll(depts);
                });

                allGovernmentEntities.addAll(governmentEntityRepository.saveAll(entitiesToSave));
            }


            if (userRepository.count() == 0) {
                List<User> users = List.of(
                        User.builder()
                                .firstName("Ministry")
                                .lastName("Admin")
                                .email("ministry.admin@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .governmentEntity(allGovernmentEntities.stream().filter(governmentEntity -> governmentEntity.getCode().equals("MOAYUSH")).findFirst().get())
                                .roles(Set.of(roleList.stream().filter(role -> role.getName().equals("MINISTRY_ADMIN")).findFirst().get()))
                                .build(),

                        User.builder()
                                .firstName("Department")
                                .lastName("Admin")
                                .email("dept.admin@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .governmentEntity(allGovernmentEntities.stream().filter(governmentEntity -> governmentEntity.getCode().equals("DOD")).findFirst().get())
                                .roles(Set.of(roleList.stream().filter(role -> role.getName().equals("DEPT_ADMIN")).findFirst().get()))
                                .build(),

                        User.builder()
                                .firstName("Department")
                                .lastName("Admin")
                                .email("indDept.admin@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .governmentEntity(allGovernmentEntities.stream().filter(governmentEntity -> governmentEntity.getCode().equals("DAE")).findFirst().get())
                                .roles(Set.of(roleList.stream().filter(role -> role.getName().equals("DEPT_ADMIN")).findFirst().get()))
                                .build(),

                        User.builder()
                                .firstName("PMO")
                                .lastName("User")
                                .email("pmo.user@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .roles(Set.of(roleList.stream().filter(role -> role.getName().equals("PMO_USER")).findFirst().get()))
                                .build(),

                        User.builder()
                                .firstName("Cabsec")
                                .lastName("User")
                                .email("cabsec.user@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .roles(Set.of(roleList.stream().filter(role -> role.getName().equals("CBSEC_USER")).findFirst().get()))
                                .build(),

                        User.builder()
                                .firstName("Super")
                                .lastName("Admin")
                                .email("super.admin@test.com")
                                .isActive(true)
                                .password(passwordEncoder.encode("password@123"))
                                .roles(Set.of(roleList.stream().filter(role -> role.getName().equals("SUPER_ADMIN")).findFirst().get()))
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
