package test.java;

import java.net.*;
import java.io.*;
import org.junit.jupiter.api.Test;
import org.json.*;
import org.apache.commons.io.FileUtils;
import main.java.VoiceIt2;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestVoiceIt2 {

  void downloadFile(String source, String destination) {
    try {
      FileUtils.copyURLToFile( new URL(source), new File(destination) );
    } catch (MalformedURLException e) {
      System.err.println(e);
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  void deleteFile(String path) {
    File file = new File(path);
     
    if(file.delete())
    {
        System.out.println("File " + path + " deleted successfully");
    }
    else
    {
        System.err.println("Failed to delete the file");
    }
  }

  int getStatus(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getInt("status");
  }

  String getResponseCode(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getString("responseCode");
  }

  String getUserId(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getString("userId");
  }

  String getGroupId(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getString("groupId");
  }

  int getEnrollmentId(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getInt("id");
  }

  int getFaceEnrollmentId(String arg) {
    JSONObject obj = new JSONObject(arg);
    return obj.getInt("faceEnrollmentId");
  }


  @Test
  void TestBasics() {
    VoiceIt2 myVoiceIt = new VoiceIt2(System.getenv("VIAPIKEY"), System.getenv("VIAPITOKEN"));
    
    String ret = "";
    // Create User
    ret = myVoiceIt.createUser();
    String userId = getUserId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Get All Users
    ret = myVoiceIt.getAllUsers();
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Check if a Specific User Exists
    ret = myVoiceIt.checkUserExists(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Create Group
    ret = myVoiceIt.createGroup("Sample Group Description");
    String groupId = getGroupId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Get All Groups
    ret = myVoiceIt.getGroupsForUser(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Add User to Group
    ret = myVoiceIt.addUserToGroup(groupId, userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Get Groups for User
    ret = myVoiceIt.getGroupsForUser(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Get a Specific Group
    ret = myVoiceIt.getGroup(groupId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Check if Group Exists
    ret = myVoiceIt.groupExists(groupId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Remove User from Group
    ret = myVoiceIt.removeUserFromGroup(groupId, userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Delete Group
    ret = myVoiceIt.deleteGroup(groupId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Delete User
    ret = myVoiceIt.deleteUser(userId);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

  }


  @Test
  void TestVideo() {
    VoiceIt2 myVoiceIt = new VoiceIt2(System.getenv("VIAPIKEY"), System.getenv("VIAPITOKEN"));
    
    String ret = "";
    ret = myVoiceIt.createUser();
    String userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    String userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    String groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Create Video Enrollments
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov", "./videoEnrollmentArmaan1.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan2.mov", "./videoEnrollmentArmaan2.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan3.mov", "./videoEnrollmentArmaan3.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov", "./videoVerificationArmaan1.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen1.mov", "./videoEnrollmentStephen1.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen2.mov", "./videoEnrollmentStephen2.mov");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen3.mov", "./videoEnrollmentStephen3.mov");

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", "./videoEnrollmentArmaan1.mov");
    int enrollmentId1 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", "./videoEnrollmentArmaan2.mov");
    int enrollmentId2 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId1, "en-US", "./videoEnrollmentArmaan3.mov");
    int enrollmentId3 = getEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", new File("./videoEnrollmentStephen1.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", new File("./videoEnrollmentStephen2.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollment(userId2, "en-US", new File("./videoEnrollmentStephen3.mov"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    
    // Video Verification
    ret = myVoiceIt.videoVerification(userId1, "en-US", "./videoVerificationArmaan1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.videoVerification(userId1, "en-US", new File("./videoVerificationArmaan1.mov"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Video Identification
    ret = myVoiceIt.videoIdentification(groupId, "en-US", "./videoVerificationArmaan1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    ret = myVoiceIt.videoIdentification(groupId, "en-US", new File("./videoVerificationArmaan1.mov"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    // Delete Individual Video Enrollments
    ret = myVoiceIt.deleteEnrollmentForUser(userId1, enrollmentId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteEnrollmentForUser(userId1, enrollmentId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteEnrollmentForUser(userId1, enrollmentId3);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Delete All Enrollments
    ret = myVoiceIt.deleteAllEnrollmentsForUser(userId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Reset for ...ByUrl operations
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);
    ret = myVoiceIt.createUser();
    userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Video Enrollments By URL
    
    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan1.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan2.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentArmaan3.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen1.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen2.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVideoEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoEnrollmentStephen3.mov");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));


    // Video Verification By URL
    ret = myVoiceIt.videoVerificationByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Video Identification By URL
    ret = myVoiceIt.videoIdentificationByUrl(groupId, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/videoVerificationArmaan1.mov");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    myVoiceIt.deleteAllEnrollmentsForUser(userId1);
    myVoiceIt.deleteAllEnrollmentsForUser(userId2);
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);

    deleteFile("./videoEnrollmentArmaan1.mov");
    deleteFile("./videoEnrollmentArmaan2.mov");
    deleteFile("./videoEnrollmentArmaan3.mov");
    deleteFile("./videoVerificationArmaan1.mov");
    deleteFile("./videoEnrollmentStephen1.mov");
    deleteFile("./videoEnrollmentStephen2.mov");
    deleteFile("./videoEnrollmentStephen3.mov");

  }


  @Test
  void TestVoice() {
    VoiceIt2 myVoiceIt = new VoiceIt2(System.getenv("VIAPIKEY"), System.getenv("VIAPITOKEN"));
    
    String ret = "";
    ret = myVoiceIt.createUser();
    String userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    String userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    String groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Create Voice Enrollments
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan1.wav", "./enrollmentArmaan1.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan2.wav", "./enrollmentArmaan2.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan3.wav", "./enrollmentArmaan3.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav", "./verificationArmaan1.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen1.wav", "./enrollmentStephen1.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen2.wav", "./enrollmentStephen2.wav");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen3.wav", "./enrollmentStephen3.wav");

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", "./enrollmentArmaan1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", "./enrollmentArmaan2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId1, "en-US", "./enrollmentArmaan3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", new File("./enrollmentStephen1.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", new File("./enrollmentStephen2.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollment(userId2, "en-US", new File("./enrollmentStephen3.wav"));
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    
    // Voice Verification
    ret = myVoiceIt.voiceVerification(userId1, "en-US", "./verificationArmaan1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.voiceVerification(userId1, "en-US", new File("./verificationArmaan1.wav"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Identification
    ret = myVoiceIt.voiceIdentification(groupId, "en-US", "./verificationArmaan1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    ret = myVoiceIt.voiceIdentification(groupId, "en-US", new File("./verificationArmaan1.wav"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    // Reset for ...ByUrl operations
    myVoiceIt.deleteAllEnrollmentsForUser(userId1);
    myVoiceIt.deleteAllEnrollmentsForUser(userId2);
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);
    ret = myVoiceIt.createUser();
    userId1 = getUserId(ret);
    ret = myVoiceIt.createUser();
    userId2 = getUserId(ret);
    ret = myVoiceIt.createGroup("Sample Group Description");
    groupId = getGroupId(ret);
    myVoiceIt.addUserToGroup(groupId, userId1);
    myVoiceIt.addUserToGroup(groupId, userId2);

    // Create Voice Enrollments By URL
    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentArmaan3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen1.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen2.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createVoiceEnrollmentByUrl(userId2, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/enrollmentStephen3.wav");
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    
    // Voice Verification
    ret = myVoiceIt.voiceVerificationByUrl(userId1, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Voice Identification
    ret = myVoiceIt.voiceIdentificationByUrl(groupId, "en-US", "https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/verificationArmaan1.wav");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));
    assertEquals(userId1, getUserId(ret));

    myVoiceIt.deleteAllEnrollmentsForUser(userId1);
    myVoiceIt.deleteAllEnrollmentsForUser(userId2);
    myVoiceIt.deleteUser(userId1);
    myVoiceIt.deleteUser(userId2);
    myVoiceIt.deleteGroup(groupId);


    deleteFile("./enrollmentArmaan1.wav");
    deleteFile("./enrollmentArmaan2.wav");
    deleteFile("./enrollmentArmaan3.wav");
    deleteFile("./verificationArmaan1.wav");
    deleteFile("./enrollmentStephen1.wav");
    deleteFile("./enrollmentStephen2.wav");
    deleteFile("./enrollmentStephen3.wav");
  }


  @Test
  void TestFace() {
    VoiceIt2 myVoiceIt = new VoiceIt2(System.getenv("VIAPIKEY"), System.getenv("VIAPITOKEN"));
    
    String ret = "";
    ret = myVoiceIt.createUser();
    String userId = getUserId(ret);
    ret = myVoiceIt.createUser();

    // Create Face Enrollments
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan1.mp4", "./faceEnrollmentArmaan1.mp4");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan2.mp4", "./faceEnrollmentArmaan2.mp4");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceEnrollmentArmaan3.mp4", "./faceEnrollmentArmaan3.mp4");
    downloadFile("https://s3.amazonaws.com/voiceit-api2-testing-files/test-data/faceVerificationArmaan1.mp4", "./faceVerificationArmaan1.mp4");


    ret = myVoiceIt.createFaceEnrollment(userId, "./faceEnrollmentArmaan1.mp4");
    int faceEnrollmentId1 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId, "./faceEnrollmentArmaan2.mp4");
    int faceEnrollmentId2 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.createFaceEnrollment(userId, "./faceEnrollmentArmaan3.mp4");
    int faceEnrollmentId3 = getFaceEnrollmentId(ret);
    assertEquals(201, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    // Face Verification
    ret = myVoiceIt.faceVerification(userId, "./faceVerificationArmaan1.mp4");
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.faceVerification(userId, new File("./faceVerificationArmaan1.mp4"));
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

   // Delete Face Enrollments Individually
    ret = myVoiceIt.deleteFaceEnrollment(userId, faceEnrollmentId1);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteFaceEnrollment(userId, faceEnrollmentId2);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    ret = myVoiceIt.deleteFaceEnrollment(userId, faceEnrollmentId3);
    assertEquals(200, getStatus(ret));
    assertEquals("SUCC", getResponseCode(ret));

    myVoiceIt.deleteUser(userId);

    deleteFile("./faceEnrollmentArmaan1.mp4");
    deleteFile("./faceEnrollmentArmaan2.mp4");
    deleteFile("./faceEnrollmentArmaan3.mp4");
    deleteFile("./faceVerificationArmaan1.mp4");

  }
}