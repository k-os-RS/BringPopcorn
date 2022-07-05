package com.cesfuencarral.bringpopcorn.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cesfuencarral.bringpopcorn.R
import com.cesfuencarral.bringpopcorn.ui.Films.FilmsFragment
import com.cesfuencarral.bringpopcorn.ui.Series.SeriesFragment
import com.cesfuencarral.bringpopcorn.ui.Settings.settings

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment
        when (position+1) {
            1-> {fragment = FilmsFragment()
                return fragment
            }
            2-> {fragment = SeriesFragment()
                return fragment
            }
            3-> {fragment = settings()
                return fragment
            }
        }

        return PlaceholderFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int =3

}