package antonborri.es.knuthansen.injection

import antonborri.es.knuthansen.ProcessFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeProcessFragment(): ProcessFragment
}