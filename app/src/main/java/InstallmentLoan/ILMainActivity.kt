/*
package InstallmentLoan

import InstallmentLoan.ilFragment.ILHomeFragment
import adapter.MenuAdapter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rupeeredee.app.R
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView
import fragment.*
import model.UserRecords
import services.SessionManager
import views.CenteredToolbar
import views.DuoDrawerLayout
import views.DuoMenuView
import widgets.DuoDrawerToggle
import java.util.*

class ILMainActivity : AppCompatActivity(), DuoMenuView.OnMenuClickListener {

    override fun onFooterClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onHeaderClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var mMenuAdapter: MenuAdapter
    internal lateinit var duoDrawerToggle: DuoDrawerToggle
    internal lateinit var bottomNavigationView: MorphBottomNavigationView
    private var mViewHolder: ViewHolder? = null
    private var mTitles = ArrayList<String>()
    private val mImage = ArrayList<Int>()
    internal lateinit var txtUSerName: TextView
    internal lateinit var txtPhone: TextView
    internal lateinit var sessionManager: SessionManager
    internal var strUserName = ""
    internal var strUserPhone: String? = ""
    internal lateinit var txtLogout: TextView
    internal var strStatus: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ilactivity_main)
        val bundle = intent.extras
        try {
            strStatus = intent.getStringExtra("status")
        } catch (e: Exception) {
            e.printStackTrace()
            strStatus = ""
        }
        sessionManager = SessionManager(this@ILMainActivity)
        mTitles = ArrayList(Arrays.asList(*resources.getStringArray(R.array.menuOptions)))
        txtUSerName = findViewById(R.id.duo_view_header_text_title)
        txtLogout = findViewById(R.id.duo_view_footer_text)
        txtLogout.setOnClickListener {
            AlertDialog.Builder(this@ILMainActivity)
                    .setMessage("Are you sure you wish to Logout ?")
                    .setCancelable(false)
                    .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                    .setPositiveButton("Confirm") { dialog, which ->
                        dialog.dismiss()
                        sessionManager.logoutUser()
                    }.show()
        }
        txtPhone = findViewById(R.id.duo_view_header_text_sub_title)
        try {
            strUserName = sessionManager.userRecord[UserRecords.firstName] + " " + sessionManager.userRecord[UserRecords.lastName]
            strUserPhone = sessionManager.userRecord[UserRecords.mobile]
            if (!strUserName.equals("", ignoreCase = true)) {
                if (!sessionManager.userRecord[UserRecords.firstName]!!.equals("", ignoreCase = true)) {
                    txtUSerName.text = strUserName
                } else {
                    txtUSerName.text = "Welcome Guest"
                }
            }
            if (!strUserPhone.equals("", ignoreCase = true)) {
                txtPhone.text = strUserPhone
            }
        } catch (e: Exception) {
            e.printStackTrace()
            strUserName = ""
            strUserPhone = ""
        }
        mImage.add(R.mipmap.dashboard)
        mImage.add(R.mipmap.about_us)
        mImage.add(R.mipmap.faq)
        mImage.add(R.mipmap.faq)
        mImage.add(R.mipmap.contact_us)
        mImage.add(R.mipmap.notification)
        mImage.add(R.mipmap.login)
        // Initialize the views
        mViewHolder = ViewHolder()
        // Handle toolbar actions
        handleToolbar()
        // Handle menu actions
        handleMenu()
        // Handle drawer actions
        handleDrawer()
        mMenuAdapter.setViewSelected(0, true)
        onOptionClicked(0, true)
        //setTitle(mTitles.get(0));
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            mViewHolder!!.mDuoDrawerLayout.closeDrawer()
            val id = menuItem.itemId
            when (id) {
                R.id.nav_home -> try {
                    if (strStatus.equals("", ignoreCase = true) || strStatus == null) {
                        mViewHolder!!.mToolbar.setTitle("Home")
                        goToFragment(ILHomeFragment(), false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    mViewHolder!!.mToolbar.setTitle("Home")
                    goToFragment(ILHomeFragment(), false)
                }
                R.id.nav_my_loan -> {
                    mViewHolder!!.mToolbar.setTitle("My Loan")
                    goToFragment(MyLoan(), false)
                }
                R.id.nav_payment -> {
                    mViewHolder!!.mToolbar.setTitle("Payment")
                    goToFragment(Payment(), false)
                }
                R.id.nav_my_profile -> {
                    mViewHolder!!.mToolbar.setTitle("My Profile")
                    goToFragment(MyProfile(), false)
                }
                R.id.nav_account -> {
                    mViewHolder!!.mToolbar.setTitle("Additional Info")
                    goToFragment(AdditionalInfo(), false)
                }
                else -> {
                }
            }
            false
        }
        try {
            if (strStatus.equals("1", ignoreCase = true) && strStatus != null) {
                bottomNavigationView.selectedItemId = R.id.nav_payment
                strStatus = ""

            } else if (strStatus.equals("2", ignoreCase = true) && strStatus != null) {
                bottomNavigationView.selectedItemId = R.id.nav_my_loan
                strStatus = ""
            } else if (strStatus.equals("3", ignoreCase = true) && strStatus != null) {
                bottomNavigationView.selectedItemId = R.id.nav_account
                strStatus = ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleToolbar() {
        setSupportActionBar(mViewHolder?.mToolbar)
    }

    private fun handleDrawer() {
        duoDrawerToggle = DuoDrawerToggle(this,
                mViewHolder?.mDuoDrawerLayout,
                mViewHolder?.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        mViewHolder?.mDuoDrawerLayout?.setDrawerListener(duoDrawerToggle)
        duoDrawerToggle.syncState()
    }

    private fun handleMenu() {
        mMenuAdapter = MenuAdapter(mTitles, mImage)
        mViewHolder?.mDuoMenuView?.setOnMenuClickListener(this)
        mViewHolder?.mDuoMenuView?.adapter = mMenuAdapter
    }

    private fun goToFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.replace(R.id.container, fragment).commitAllowingStateLoss()
    }

    override fun onOptionClicked(position: Int, objectClicked: Any) {
        // Navigate to the right fragment
        when (position) {
            0 -> try {
                if (strStatus.equals("", ignoreCase = true) || strStatus == null) {
                    mMenuAdapter.setViewSelected(0, true)
                    mViewHolder?.mToolbar!!.title = "Home"
                    goToFragment(ILHomeFragment(), false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mViewHolder?.mToolbar!!.title = "Home"
                goToFragment(ILHomeFragment(), false)
            }
            1 -> {
                title = "About us"
                mMenuAdapter.setViewSelected(1, true)
                goToFragment(AboutUsFragment(), false)
            }
            2 -> {
                title = "Track Your Application"
                mMenuAdapter.setViewSelected(2, true)
                goToFragment(FragmentTrackApplication(), false)
            }

            3 -> {
                title = "FAQs"
                mMenuAdapter.setViewSelected(3, true)
                goToFragment(FaqsFragment(), false)
            }
            4 -> {
                title = "Contact Us"
                mMenuAdapter.setViewSelected(4, true)
                goToFragment(ContactUs(), false)
            }
            5 -> {
                title = "Notification"
                mMenuAdapter.setViewSelected(5, true)
                goToFragment(NotificationFragment(), false)
            }
            else -> title = mTitles[position]
        }
        // Close the drawer
        mViewHolder?.mDuoDrawerLayout?.closeDrawer()
    }

    private inner class ViewHolder internal constructor() {
        val mDuoDrawerLayout: DuoDrawerLayout
        val mDuoMenuView: DuoMenuView
        val mToolbar: CenteredToolbar

        init {
            mDuoDrawerLayout = findViewById<View>(R.id.drawer) as DuoDrawerLayout
            mDuoMenuView = mDuoDrawerLayout.menuView as DuoMenuView
            mToolbar = findViewById(R.id.toolbar)
        }
    }
}*/
