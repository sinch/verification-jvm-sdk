package com.sinch.verificationcore.method

import com.sinch.verification.model.VerificationState
import com.sinch.verification.model.VerificationStateStatus
import com.sinch.verification.model.initiation.InitiationResponseData
import com.sinch.verification.model.verification.VerificationResponseData
import com.sinch.verification.model.verification.VerificationStatus
import com.sinch.verification.network.VerificationService
import com.sinch.verification.network.auth.AppKeyAuthorizationMethod
import com.sinch.verification.network.service.RetrofitRestServiceProvider
import com.sinch.verification.process.config.VerificationMethodConfig
import com.sinch.verification.process.listener.InitiationListener
import com.sinch.verification.process.listener.VerificationListener
import com.sinch.verification.process.method.VerificationMethod
import com.sinch.verificationcore.TestConstants
import com.sinch.verificationcore.TestConstants.TEST_DEFAULT_METHOD
import com.sinch.verificationcore.TestConstants.TEST_PHONE_NUMBER
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.mock.Calls

class VerificationMethodTests {

    @MockK
    lateinit var initiationListener: InitiationListener

    @MockK
    lateinit var verificationListener: VerificationListener

    @MockK
    lateinit var mockedVerificationService: VerificationService

    private val mockedServiceProvider: RetrofitRestServiceProvider by lazy {
        spyk(RetrofitRestServiceProvider(authorizationMethod = AppKeyAuthorizationMethod(TestConstants.TEST_APP_KEY)))
    }

    companion object {
        val testConfig = VerificationMethodConfig.Builder
            .instance
            .authorizationMethod(AppKeyAuthorizationMethod(TestConstants.TEST_APP_KEY))
            .verificationMethod(TEST_DEFAULT_METHOD)
            .number(TEST_PHONE_NUMBER)
            .build()
    }

    private val verification by lazy {
        VerificationMethod(
            config = testConfig,
            initiationListener = initiationListener,
            verificationListener = verificationListener,
            serviceProvider = mockedServiceProvider
        )
    }

    @Before
    fun setupUp() {
        MockKAnnotations.init(this, relaxed = true)
        every { mockedServiceProvider.createService(VerificationService::class.java) } returns mockedVerificationService
    }

    @Test
    fun testInitialState() {
        Assert.assertEquals(verification.verificationState, VerificationState.IDLE)
    }

    @Test
    fun testSuccessfulInitialization() {
        val response = mockk<InitiationResponseData>(relaxed = true)
        every { mockedVerificationService.initializeVerification(any(), any()) }.returns(
            Calls.response(response)
        )
        verification.initiate()
        Assert.assertEquals(
            VerificationState.Initialization(VerificationStateStatus.SUCCESS),
            verification.verificationState
        )
        verify { initiationListener.onInitiated(any()) }
    }

    @Test
    fun testErrorInitialization() {
        val error = mockk<Throwable>()
        every { mockedVerificationService.initializeVerification(any(), any()) }.returns(
            Calls.failure(error)
        )
        verification.initiate()
        Assert.assertEquals(
            VerificationState.Initialization(VerificationStateStatus.ERROR),
            verification.verificationState
        )
        verify { initiationListener.onInitializationFailed(error) }
    }

    @Test
    fun testCorrectVerificationFlowNotifiesListener() {
        val correctCode = "123456"
        setupVerificationMock(correctCode)
        verification.initiate()
        verification.verify(correctCode)

        verify(exactly = 0) { verificationListener.onVerificationFailed(any()) }
        verify(exactly = 1) { verificationListener.onVerified() }
    }

    @Test
    fun testWrongCodeVerificationFlowNotifiesListener() {
        val correctCode = "123456"
        setupVerificationMock(correctCode)
        verification.initiate()
        verification.verify(correctCode.reversed())

        verify(exactly = 1) { verificationListener.onVerificationFailed(any()) }
        verify(exactly = 0) { verificationListener.onVerified() }
    }

    @Test
    fun testFailureStatusNotifiesCorrectListener() {
        val correctCode = "123456"
        setupVerificationMock(correctCode = correctCode, returnedStatus = VerificationStatus.FAILED)
        verification.initiate()
        verification.verify(correctCode)
        verify(exactly = 1) { verificationListener.onVerificationFailed(any()) }
        verify(exactly = 0) { verificationListener.onVerified() }
    }

    @Test
    fun testMultipleVerificationNotifyListenerOnce() {
        val correctCode = "123456"
        setupVerificationMock(correctCode = correctCode)
        verification.initiate()
        for (i in 0..10) {
            verification.verify(correctCode)
        }
        verify(exactly = 0) { verificationListener.onVerificationFailed(any()) }
        verify(exactly = 1) { verificationListener.onVerified() }
    }

    private fun setupVerificationMock(
        correctCode: String,
        returnedStatus: VerificationStatus = VerificationStatus.SUCCESSFUL
    ) {
        val response = mockk<InitiationResponseData>(relaxed = true)
        every { mockedVerificationService.initializeVerification(any(), any()) }.returns(
            Calls.response(response)
        )

        val mockedVerResponse = mockk<VerificationResponseData>(relaxed = true) {
            every { status } returns returnedStatus
        }

        every { mockedVerificationService.verifyNumber(any(), any()) }.returns(
            Calls.failure(mockk())
        )
        every {
            mockedVerificationService.verifyNumber(any(), match {
                it.smsDetails?.code == correctCode
            })
        }.returns(
            Calls.response(mockedVerResponse)
        )
    }

}