package runners;
import com.intuit.karate.junit5.Karate;
public class UsersRunner {
//    @Karate.Test
//    Karate runUsers() {
//        return Karate.run("classpath:features/user.feature");
//    }
//    @Karate.Test
//    Karate runUsers() {
//        return Karate.run("classpath:features/usersJSONData.feature");
//    }
//    @Karate.Test
//    Karate runAllWithFolderName() {
//        return Karate.run("classpath:features");
//    }
//    @Karate.Test
//    Karate runAllWithIndividualFilenames() {
//        return Karate.run("classpath:features/user.feature",
//                "classpath:features/usersCSVData.feature",
//                "classpath:features/usersJSONData.feature");
//    }


    @Karate.Test
    Karate runSmoke() {
        return Karate.run("classpath:features").tags("@smoke");
    }
}