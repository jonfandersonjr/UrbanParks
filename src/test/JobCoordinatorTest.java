package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.JobCoordinator;
<<<<<<< HEAD
import model.ParkManager;
=======
import model.OfficeStaff;
import model.ParkManager;
import model.Volunteer;
>>>>>>> e6868a3348b84c5451296eb5bc245674453ffa3d

public class JobCoordinatorTest {
    public JobCoordinator globalJobCoordinator;
    public Volunteer globalVolunteerJane;
    public ParkManager globalParkManagerSam;
    public OfficeStaff globalOfficeStaffAlex;
    public Job anyOldValidJob;
    public ParkManager anyParkManager;

    @Before
    public void setUp() throws Exception {
        globalJobCoordinator = new JobCoordinator();
        globalVolunteerJane = new Volunteer("Jane");
        globalParkManagerSam = new ParkManager("Sam");
        globalOfficeStaffAlex = new OfficeStaff("Alex");
        anyOldValidJob = new Job("anyOldValidJob");
        anyParkManager = new ParkManager("anyOldPM");
    }

    // Business Rule:
    // There can not be more than the maximum number of pending jobs at a time
    // in the entire system, default of 20

    // The system has far fewer than the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs_LessThanMaximumPendingJobs_ShouldReturnTrue() {
        // add 0 or 1 job so that # current jobs < maximum jobs
        globalJobCoordinator.addPendingJob(anyOldValidJob);

        assertTrue(globalJobCoordinator.hasSpaceToAddJobs());
    }

    // The system has one fewer than the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs__OneLessThanMaximumPendingJobs__ShouldReturnTrue() {
        // add 20 - 1 = 19 jobs to pending
        for (int i = 0; i < JobCoordinator.MAXIMUM_JOBS - 1; i++) {
            Job generatedValidJob = new Job("generatedValidJob" + i);
            globalJobCoordinator.addPendingJob(generatedValidJob);
        }

        assertTrue(globalJobCoordinator.hasSpaceToAddJobs());
    }

    // The system has exactly the maximum number of pending jobs
    @Test
    public final void hasSpaceToAddJobs__ExactlyMaximumPendingJobs__ShouldReturnFalse() {
        // add exactly 20 (maximum) jobs to pending
        for (int i = 0; i < 20; i++) {
            Job generatedValidJob = new Job("generatedValidJob" + i);
            globalJobCoordinator.addPendingJob(generatedValidJob);
        }

        assertFalse(globalJobCoordinator.hasSpaceToAddJobs());
    }
    
    // Business Rule:
    // No job can be specified that takes more than the maximum number of days
    
    // The specified job takes one fewer than the maximum number of days
    @Test
    public final void canAddJob_JobOneLessThanMaxJobLength_ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar validDateOneWeekAhead = (GregorianCalendar) currentDate.clone();
        validDateOneWeekAhead.add(GregorianCalendar.DAY_OF_YEAR, 7);
        GregorianCalendar validDateOneWeekAheadPlusOneLessThanMaxLength = 
                        (GregorianCalendar) validDateOneWeekAhead.clone();
        validDateOneWeekAheadPlusOneLessThanMaxLength
            .add(GregorianCalendar.DAY_OF_YEAR, JobCoordinator.MAXIMUM_JOB_LENGTH - 1);
        
        Job lessThanMaxLengthJob = new Job("lessThanMaxLengthJob");
        lessThanMaxLengthJob.setStartDate(validDateOneWeekAhead);
        lessThanMaxLengthJob.setEndDate(validDateOneWeekAheadPlusOneLessThanMaxLength);
        
        assertTrue(globalJobCoordinator.checkIfLegalToAddJob(lessThanMaxLengthJob, anyParkManager) == 0);
        
    }
    
    // The specified job takes the maximum number of days
    @Test
    public final void canAddJob_JobMaxJobLength_ShouldBeTrue() {        
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar validDateOneWeekAhead = (GregorianCalendar) currentDate.clone();
        validDateOneWeekAhead.add(GregorianCalendar.DAY_OF_YEAR, 7);
        GregorianCalendar validDateOneWeekAheadPlusMaxLength = 
                        (GregorianCalendar) validDateOneWeekAhead.clone();
        validDateOneWeekAheadPlusMaxLength
            .add(GregorianCalendar.DAY_OF_YEAR, JobCoordinator.MAXIMUM_JOB_LENGTH);
        
        Job exactlyMaxLengthJob = new Job("exactlyMaxLengthJob");
        exactlyMaxLengthJob.setStartDate(validDateOneWeekAhead);
        exactlyMaxLengthJob.setEndDate(validDateOneWeekAheadPlusMaxLength);
        
        assertTrue(globalJobCoordinator.checkIfLegalToAddJob(exactlyMaxLengthJob, anyParkManager) == 0);
    }
    
    // The specified job takes one more than the maximum number of days
    @Test
    public final void canAddJob_JobOneMoreThanMaxJobLength_ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar validDateOneWeekAhead = (GregorianCalendar) currentDate.clone();
        validDateOneWeekAhead.add(GregorianCalendar.DAY_OF_YEAR, 7);
        GregorianCalendar validDateOneWeekAheadPlusOneMoreThanMaxLength = 
                        (GregorianCalendar) validDateOneWeekAhead.clone();
        validDateOneWeekAheadPlusOneMoreThanMaxLength
            .add(GregorianCalendar.DAY_OF_YEAR, JobCoordinator.MAXIMUM_JOB_LENGTH + 1);
        
        Job moreThanMaxLengthJob = new Job("moreThanMaxLengthJob");
        moreThanMaxLengthJob.setStartDate(validDateOneWeekAhead);
        moreThanMaxLengthJob.setEndDate(validDateOneWeekAheadPlusOneMoreThanMaxLength);

        assertFalse(globalJobCoordinator.checkIfLegalToAddJob(moreThanMaxLengthJob, anyParkManager) == 0);
        
    }

    // Business Rule:
    // No job can be specified whose end date is more than the maximum number of
    // days from the current date, default of 75

    // The specified job ends one fewer than the maximum number of days from the
    // current date
    @Test
    public final void canAddJob_JobEndsOneLessThanMaximumDays__ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar oneLessThanMaximumDaysAwayDate =
                        (GregorianCalendar) currentDate.clone();
        oneLessThanMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR,
                                           JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB - 1);

        // specify generic job on that day
        Job jobOneLessThanMaximumDaysAway = new Job("jobOneLessThanMaximumDaysAway");
        jobOneLessThanMaximumDaysAway.setStartDate(oneLessThanMaximumDaysAwayDate);
        jobOneLessThanMaximumDaysAway.setEndDate(oneLessThanMaximumDaysAwayDate);

        // test
        assertTrue(globalJobCoordinator.checkIfLegalToAddJob(jobOneLessThanMaximumDaysAway, anyParkManager) == 0);
    }

    // The specified job ends the maximum number of days from the current date
    @Test
    public final void canAddJob_JobEndsExactlyMaximumDaysAway__ShouldBeTrue() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar exactlyMaximumDaysAwayDate = (GregorianCalendar) currentDate.clone();
        exactlyMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR,
                                       JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB);

        // specify generic job on that day
        Job jobExactlyMaximumDaysAway = new Job("jobExactlyMaximumDaysAway");
        jobExactlyMaximumDaysAway.setStartDate(exactlyMaximumDaysAwayDate);
        jobExactlyMaximumDaysAway.setEndDate(exactlyMaximumDaysAwayDate);

        // test
        assertTrue(globalJobCoordinator.checkIfLegalToAddJob(jobExactlyMaximumDaysAway, anyParkManager) == 0);
    }

    // The specified job ends one more than the maximum number of days from the
    // current date
    @Test
    public final void canAddJob_JobEndsOneMoreThanMaximumDays__ShouldBeFalse() {
        GregorianCalendar currentDate = globalJobCoordinator.getCurrentDate();

        // create new calendar date to add to generic job
        GregorianCalendar oneMoreThanMaximumDaysAwayDate =
                        (GregorianCalendar) currentDate.clone();
        oneMoreThanMaximumDaysAwayDate.add(GregorianCalendar.DAY_OF_YEAR,
                                           JobCoordinator.MAXIMUM_DAYS_AWAY_TO_POST_JOB + 1);

        // specify generic job on that day
        Job jobOneMoreThanMaximumDaysAway = new Job("jobOneMoreThanMaximumDaysAway");
        jobOneMoreThanMaximumDaysAway.setStartDate(oneMoreThanMaximumDaysAwayDate);
        jobOneMoreThanMaximumDaysAway.setEndDate(oneMoreThanMaximumDaysAwayDate);

        // test
        assertFalse(globalJobCoordinator.checkIfLegalToAddJob(jobOneMoreThanMaximumDaysAway, anyParkManager) == 0);
    }
    
    
    @Test
    public final void getJobListing_NoJobsWithAnyUser_ShouldBeEmpty() {
        assertTrue(globalJobCoordinator.getJobListing(globalVolunteerJane).isEmpty());
        assertTrue(globalJobCoordinator.getJobListing(globalParkManagerSam).isEmpty());
        assertTrue(globalJobCoordinator.getJobListing(globalOfficeStaffAlex).isEmpty());
    }
    
    @Test
    public final void getJobListing_OneFutureJobWithVolunteer_ShouldOnlyHaveFutureJob() {
        fail("Not Yet Implemented");

        assertTrue(globalJobCoordinator.getJobListing(globalVolunteerJane).isEmpty());
    }
    
    @Test
    public final void getJobListing_OnePastJobWithVolunteer_ShouldBeEmpty() {
        fail("Not Yet Implemented");

        assertTrue(globalJobCoordinator.getJobListing(globalVolunteerJane).isEmpty());
    }
    
    @Test
    public final void getJobListing_OneFutureOnePastJobWithVolunteer_ShouldOnlyHaveFutureJob() {
        Job futureJob = new Job("Job in the Future");
        GregorianCalendar futureDate = new GregorianCalendar();
        futureDate.add(GregorianCalendar.DAY_OF_YEAR, 7);
        globalJobCoordinator.addPendingJob(futureJob);

        assertTrue(globalJobCoordinator.getJobListing(globalVolunteerJane).isEmpty());
    }

    @Test
    public final void getJobListing_OneFutureJobWithParkManager_ShouldOnlyHaveFutureJob() {
        fail("Not Yet Implemented");
    }
    
    @Test
    public final void getJobListing_OnePastJobWithParkManager_ShouldBeEmpty() {
        fail("Not Yet Implemented");
    }
    
    @Test
    public final void getJobListing_OneFutureOnePastJobWithParkManager_ShouldOnlyHaveFutureJob() {
        fail("Not Yet Implemented");
    }
    
    @Test
    public final void getJobListing_OneFutureJobWithOfficeStaff_ShouldOnlyHaveFutureJob() {
        fail("Not Yet Implemented");
    }
    
    @Test
    public final void getJobListing_OnePastJobWithOfficeStaff_ShouldBeEmpty() {
        fail("Not Yet Implemented");
    }
    
    @Test
    public final void getJobListing_OneFutureOnePastJobWithOfficeStaff_ShouldHaveBothJobs() {
        fail("Not Yet Implemented");
    }
}
