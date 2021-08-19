package com.example.calendarprojectteamlinkot.models

import android.util.Log
import com.example.calendarprojectteamlinkot.enums.Days
import com.example.calendarprojectteamlinkot.enums.Month


class Constants {
    companion object
    {
       // var defaultMaxDay = 0
        var startOfCounting = 0 //Jan 2021
        var numOfBeforeMonth = 0
        var endOfCounting = 0

        fun defaultKalendarList(): ArrayList<MonthModel>
        {
            val kalendarList = ArrayList<MonthModel>()

            val january = MonthModel(1,2021, makeDaysOfMonth(Month.January,26,5,31),
                                        makeDaysOfWeek(),"Happy",false,false,Month.January)

            kalendarList.add(january)

            val february = MonthModel(2,2021, makeDaysOfMonth(Month.February,30,12,31),
                makeDaysOfWeek(),"Happy",false,false,Month.February)

            kalendarList.add(february)

            val march = MonthModel(3,2021, makeDaysOfMonth(Month.March, 27,9,27),
                                        makeDaysOfWeek(),"Happy",false,false,Month.March)

            kalendarList.add(march)

            val april = MonthModel(4,2021, makeDaysOfMonth(Month.April, 27,7,31),
                makeDaysOfWeek(),"Happy",false,false,Month.April)

            kalendarList.add(april)

            val may = MonthModel(5,2021, makeDaysOfMonth(Month.May, 24,4,29),
                makeDaysOfWeek(),"Happy",false,false,Month.May)

            kalendarList.add(may)

            val june = MonthModel(6,2021, makeDaysOfMonth(Month.June, 29,9,30),
                makeDaysOfWeek(),"Happy",false,false,Month.June)

            kalendarList.add(june)

            val july = MonthModel(7,2021, makeDaysOfMonth(Month.July, 26,9,29),
                makeDaysOfWeek(),"Happy",false,false,Month.July)

            kalendarList.add(july)

            val august = MonthModel(8,2021, makeDaysOfMonth(Month.August, 0,10,31),
                makeDaysOfWeek(),"Happy",false,false,Month.August)

            kalendarList.add(august)

            val september = MonthModel(9,2021, makeDaysOfMonth(Month.September, 28,8,30),
                makeDaysOfWeek(),"Happy",false,false,Month.September)

            kalendarList.add(september)

            val october = MonthModel(10,2021, makeDaysOfMonth(Month.October, 25,5,29),
                makeDaysOfWeek(),"Happy",false,false,Month.October)

            kalendarList.add(october)

            val november = MonthModel(11,2021, makeDaysOfMonth(Month.November, 30,10,31),
                makeDaysOfWeek(),"Happy",false,false,Month.November)

            kalendarList.add(november)

            val december = MonthModel(12,2021, makeDaysOfMonth(Month.December, 25,7,29),
                makeDaysOfWeek(),"Happy",false,false,Month.December)

            kalendarList.add(december)

            return kalendarList
        }

        fun makeDaysOfMonth(month: Month,startOfCounting: Int, endOfCounting: Int, numOfBeforeMonth:Int): ArrayList<Int>
        {
            this.startOfCounting = startOfCounting
            this.numOfBeforeMonth = numOfBeforeMonth
            //var defaultMaxDay = 30
            var day = 0
            this.endOfCounting = endOfCounting
            val daysOfJanuary = arrayListOf<Int>()
            var isAlready31 = false

            if (month == Month.January || month == Month.May || month == Month.July ||
                month == Month.August || month == Month.October || month == Month.December)  {
                day = 31

                var d = numOfBeforeMonth
                var x = true

                while (this.startOfCounting <= d)
                {
                    this.startOfCounting++
                    daysOfJanuary.add(this.startOfCounting)
                    if(this.startOfCounting >= day && x)
                    {

                        this.startOfCounting = 0
                        d = day
                        isAlready31 = true
                        x = false

                    }else if((this.startOfCounting >= 30 &&  (month == Month.May || month == Month.July || month == Month.October || month == Month.December)) && x)
                    {

                        this.startOfCounting = 0
                        d = day
                        isAlready31 = true
                        x = false

                    }

                    if(this.startOfCounting >= day && isAlready31)
                    {
                        this.startOfCounting = 0
                        d = this.endOfCounting
                    }
                }
            }else if (month == Month.February)
            {
                day = 28
                var d = numOfBeforeMonth

                while (this.startOfCounting <= d)
                {
                    if(this.startOfCounting >= day)
                    {
                        this.startOfCounting++
                        daysOfJanuary.add(this.startOfCounting)
                        this.startOfCounting = 0
                        d = day
                        isAlready31 = true
                    }
                    this.startOfCounting++
                    daysOfJanuary.add(this.startOfCounting)
                    if(this.startOfCounting >= day && isAlready31)
                    {
                        this.startOfCounting = 0
                        d = this.endOfCounting
                    }
                }

            }else if (month == Month.April || month == Month.June || month == Month.September || month == Month.November) {
                day = 30
                var d = numOfBeforeMonth

                while (this.startOfCounting <= d)
                {
                    if(this.startOfCounting >= day)
                    {
                        this.startOfCounting++
                        daysOfJanuary.add(this.startOfCounting)
                        this.startOfCounting = 0
                        d = day
                        isAlready31 = true
                    }
                    this.startOfCounting++
                    daysOfJanuary.add(this.startOfCounting)
                    if(this.startOfCounting >= day && isAlready31)
                    {
                        this.startOfCounting = 0
                        d = this.endOfCounting
                    }
                }
            }else{
                var x = true
                if(month == Month.March)
                {
                    day = 30

                    var d = numOfBeforeMonth


                    while (this.startOfCounting <= d) {

                        this.startOfCounting++
                        daysOfJanuary.add(this.startOfCounting)
                        if (this.startOfCounting >= 27 && x)
                        {
                            this.startOfCounting = 0
                            d = day
                            x = false
                            isAlready31 = true
                        }

                        if(this.startOfCounting >= day+1 && isAlready31)
                        {
                            this.startOfCounting = 0
                            d = this.endOfCounting
                            isAlready31 = false
                        }

                    }
                }
            }

            return daysOfJanuary
        }

        fun makeDaysOfWeek(): ArrayList<String>
        {
            val daysOfWeek = ArrayList<String>()
            daysOfWeek.add(Days.SUN.toString())
            daysOfWeek.add(Days.MON.toString())
            daysOfWeek.add(Days.TUE.toString())
            daysOfWeek.add(Days.WED.toString())
            daysOfWeek.add(Days.THU.toString())
            daysOfWeek.add(Days.FRI.toString())
            daysOfWeek.add(Days.SAT.toString())

            return  daysOfWeek
        }

        fun makeYear(): ArrayList<Int>
        {
            val years = ArrayList<Int>()
            years.add(2021)
            years.add(2020)
            years.add(2019)
            years.add(2018)

            return years
        }

    }//End of Companion Object
}