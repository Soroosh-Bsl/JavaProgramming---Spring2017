import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class EZStds implements Comparable<EZStds>
{
    int ID;
    double mark;

    public int compareTo(EZStds other)
    {
        if (this.mark > other.mark)
            return -1;
        if (this.mark == other.mark)
        {
            if (this.ID > other.ID)
                return -1;
            return 1;
        }
        return 1;
    }
}
class EducationSystem
{
    ArrayList<Student> students = new ArrayList<>();
    ArrayList<Teacher> teachers = new ArrayList<>();
    ArrayList<Course> courses = new ArrayList<>();

    void addStudent(int stId)
    {
        Student temp = new Student(stId);
        students.add(temp);
    }

    void W(int courseId, int stId)
    {
        int indexOfCourse = 0;
        boolean courseValid = false;

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                courseValid = true;
                indexOfCourse = i;
                break;
            }
        }
        for (int i = 0; i < students.size(); i++)
        {
            if (courseValid && students.get(i).ID == stId && students.get(i).gotCourses.contains(courses.get(indexOfCourse)))
            {
                students.get(i).numberOfW += courses.get(indexOfCourse).units;
                if (students.get(i).numberOfW <= 3)
                {
                    students.get(i).marks.remove(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse)));
                    students.get(i).gotCourses.remove(courses.get(indexOfCourse));
                    students.get(i).gotUnits -= courses.get(indexOfCourse).units;
                    courses.get(indexOfCourse).numOfStudents--;
                }
            }
        }
    }

    void reg(int stId, int courseId)
    {
        boolean isStudent = false, isCourse = false;
        int indexOfStudent = 0, indexOfCourse = 0;

        for (int i = 0; i < students.size(); i++)
        {
            if (students.get(i).ID == stId)
            {
                isStudent = true;
                indexOfStudent = i;
                break;
            }
        }

        for (int i = 0; i < courses.size(); i++)
        {
            if (isStudent && courses.get(i).ID == courseId && courses.get(i).numOfStudents < courses.get(i).capacity)
            {
                isCourse = true;
                indexOfCourse = i;
                break;
            }
        }

        if (isCourse && isStudent)
        {
            students.get(indexOfStudent).gotCourses.add(courses.get(indexOfCourse));
            students.get(indexOfStudent).marks.add(-1.);
            students.get(indexOfStudent).gotUnits += courses.get(indexOfCourse).units;
            courses.get(indexOfCourse).numOfStudents++;

        }
    }

    void addCap(int teacherId, int courseId, int capaciity)
    {
        boolean isTeacher = false, isCourse = false;
        int indexOfTeacher = 0, indexOfCourse = 0;

        for (int i = 0; i < teachers.size(); i++)
        {
            if (teachers.get(i).ID == teacherId)
            {
                isTeacher = true;
                indexOfTeacher = i;
                break;
            }
        }

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                isCourse = true;
                indexOfCourse = i;
                break;
            }
        }

        if (isCourse && isTeacher)
        {
            if (teachers.get(indexOfTeacher).teachingCourses.contains(courses.get(indexOfCourse)))
            {
                courses.get(indexOfCourse).capacity += capaciity;
            }
        }
    }

    void markAll(int teacherId, int courseId, double mark)
    {
        boolean isTeacher = false, isCourse = false;
        int indexOfTeacher = 0, indexOfCourse = 0;

        for (int i = 0; i < teachers.size(); i++)
        {
            if (teachers.get(i).ID == teacherId)
            {
                isTeacher = true;
                indexOfTeacher = i;
                break;
            }
        }

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                isCourse = true;
                indexOfCourse = i;
                break;
            }
        }

        if (isCourse && isTeacher)
        {
            for (int i = 0; i < students.size(); i++)
            {
                if (students.get(i).gotCourses.contains(courses.get(indexOfCourse)) && teachers.get(indexOfTeacher).teachingCourses.contains(courses.get(indexOfCourse)))
                {
                    students.get(i).marks.add(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse)), mark);
                    students.get(i).marks.remove(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse)) + 1);
                }
            }
        }

    }

    void pureOghde1_1(int teacherId, int courseId, int stId)
    {
        boolean isTeacher = false, isCourse = false, isStudent = false;
        int indexOfTeacher = 0, indexOfCourse = 0, indexOfStd = 0;

        for (int i = 0; i < teachers.size(); i++)
        {
            if (teachers.get(i).ID == teacherId)
            {
                isTeacher = true;
                indexOfTeacher = i;
                break;
            }
        }

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                isCourse = true;
                indexOfCourse = i;
                break;
            }
        }

        for (int i = 0; i < students.size(); i++)
        {
            if (students.get(i).ID == stId)
            {
                isStudent = true;
                indexOfStd = i;
                break;
            }
        }

        if (!isStudent)
        {
            System.out.println("sathsathsath");
        }

        if (isCourse && isStudent && isTeacher && teachers.get(indexOfTeacher).teachingCourses.contains(courses.get(indexOfCourse)))
        {
            if (students.get(indexOfStd).gotCourses.contains(courses.get(indexOfCourse)));
            {
                students.get(indexOfStd).marks.add(students.get(indexOfStd).gotCourses.indexOf(courses.get(indexOfCourse)), 9.9);
                students.get(indexOfStd).marks.remove(students.get(indexOfStd).gotCourses.indexOf(courses.get(indexOfCourse)) + 1);
            }
        }
    }

    void pureOghde1_all(int teacherId, int courseId)
    {
        boolean isTeacher = false, isCourse = false;
        int indexOfCourse = 0, indexOfteacher = 0;

        for (int i = 0; i < teachers.size(); i++)
        {
            if (teachers.get(i).ID == teacherId)
            {
                isTeacher = true;
                indexOfteacher = i;
                break;
            }
        }

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                isCourse = true;
                indexOfCourse = i;
                break;
            }
        }

        if (isCourse && isTeacher && teachers.get(indexOfteacher).teachingCourses.contains(courses.get(indexOfCourse)))
        {
            for (int i = 0; i < students.size(); i++)
            {
                if (students.get(i).gotCourses.contains(courses.get(indexOfCourse)) == true);
                {
                    if (students.get(i).gotCourses.contains(courses.get(indexOfCourse)))
                    {
                        students.get(i).marks.add(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse)), 9.9);
                        students.get(i).marks.remove(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse)) + 1);
                    }
                }
            }
        }
    }

    void pureOghdeAll_all(int teacherId)
    {
        boolean isTeacher = false;
        int indexOfTeacher = 0;

        for (int i = 0; i < teachers.size(); i++)
        {
            if (teachers.get(i).ID == teacherId)
            {
                isTeacher = true;
                indexOfTeacher = i;
                break;
            }
        }

        if (isTeacher)
        {
            for (int i = 0; i < students.size(); i++)
            {
                for (int j = 0; j < students.get(i).gotCourses.size(); j++)
                {
                    if (teachers.get(indexOfTeacher).teachingCourses.contains(students.get(i).gotCourses.get(j)))
                    {
                        students.get(i).marks.add(students.get(i).gotCourses.indexOf(students.get(i).gotCourses.get(j)), 9.9);
                        students.get(i).marks.remove(students.get(i).gotCourses.indexOf(students.get(i).gotCourses.get(j)) + 1);
                    }
                }
            }

        }
    }

    void addCourse(int courseId, int units)
    {
        Course temp = new Course(courseId, units);
        courses.add(temp);
    }

    void addLecturer(int teacherId, int ... courses)
    {
        if (courses.length > 0)
        {
            Teacher temp = new Teacher(teacherId);

            for (int i = 0; i < courses.length; i++)
            {
                boolean flag = false;
                for (int j = 0; j < this.courses.size(); j++)
                {
                    if (this.courses.get(j).ID == courses[i] && !this.courses.get(j).gotLt)
                    {
                        flag = true;
                        temp.teachingCourses.add(this.courses.get(j));
                        this.courses.get(j).gotLt = true;
                    }
                }
                if (!flag)
                {
                    int index = this.courses.size();
                    addCourse(courses[i], 3);
                    this.courses.get(index).gotLt = true;
                    temp.teachingCourses.add(this.courses.get(index));
                }

            }
            teachers.add(temp);
        }
        else
        {
            Teacher temp = new Teacher(teacherId);

            teachers.add(temp);
        }
    }

    void markTaki(int teacherId, int courseId, int stId, double mark)
    {
        boolean isTeacher = false, isCourse = false, isStudent = false;
        int indexOfCourse = 0, indexOfTeacher = 0, indexOfStd = 0;

        for (int i = 0; i < teachers.size(); i++)
        {
            if (teachers.get(i).ID == teacherId)
            {
                isTeacher = true;
                indexOfTeacher = i;
                break;
            }
        }

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                isCourse = true;
                indexOfCourse = i;
                break;
            }
        }

        for (int i = 0; i < students.size(); i++)
        {
            if (students.get(i).ID == stId)
            {
                isStudent = true;
                indexOfStd = i;
                break;
            }
        }

        if (isCourse && isTeacher && isStudent)
        {
            if (teachers.get(indexOfTeacher).teachingCourses.contains(courses.get(indexOfCourse)))
            {
                if (students.get(indexOfStd).gotCourses.contains(courses.get(indexOfCourse)))
                {
                    students.get(indexOfStd).marks.add(students.get(indexOfStd).gotCourses.indexOf(courses.get(indexOfCourse)), mark);
                    students.get(indexOfStd).marks.remove(students.get(indexOfStd).gotCourses.indexOf(courses.get(indexOfCourse)) + 1);
                }
            }
        }

    }
    void EZ(int stId)
    {
        boolean isStudent = false;
        for (int i = 0; i < students.size(); i++)
        {
            if (students.get(i).ID == stId)
            {
                isStudent = true;
                students.get(i).EZ();
                students.remove(students.get(i));
            }
        }
    }

    void showCourse(int courseId, String str)
    {
        boolean isCourse = false;
        int indexOfCourse = 0;
        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                indexOfCourse = i;
                isCourse = true;
                break;
            }
        }

        if (isCourse)
        {
            ArrayList<Integer> print = new ArrayList<>();
            if (str.equals("st"))
            {
                for (int i = 0; i < students.size(); i++)
                {
                    if (students.get(i).gotCourses.contains(courses.get(indexOfCourse)))
                    {
                        print.add(students.get(i).ID);
                    }
                }
                Collections.sort(print);
                for (int i = 0; i < print.size(); i++)
                {
                    System.out.print(print.get(i) + " ");
                }
                print.clear();
            }


            else if (str.equals("lt"))
            {
                for (int i = 0; i < teachers.size(); i++)
                {
                    if (teachers.get(i).teachingCourses.contains(courses.get(indexOfCourse)))
                    {
                        print.add(teachers.get(i).ID)   ;
                    }
                }
                Collections.sort(print);
                for (int i = 0; i < print.size(); i++)
                {
                    System.out.print(print.get(i) + " ");
                }
                print.clear();
            }

            else if (str.equals("cap"))
            {
                System.out.print(courses.get(indexOfCourse).capacity);
            }

            else if (str.equals("avg"))
            {
                double sum = 0.;
                int size = 0;

                for (int i = 0; i < students.size(); i++)
                {
                    if (students.get(i).gotCourses.contains(courses.get(indexOfCourse)))
                    {
                        sum += students.get(i).marks.get(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse)));
                        size++;
                    }
                }
                System.out.format("%.1f", sum/size);
            }
        }
        else
            System.out.print("sathsathsath");

        System.out.println();
    }

    void showAvg(int stId)
    {
        boolean isStudent = false;

        for (int i = 0; i < students.size(); i++)
        {
            if (students.get(i).ID == stId)
            {
                isStudent = true;
                System.out.format("%.1f\n", students.get(i).AVG());
            }
        }
        if (!isStudent)
        {
            System.out.println("sathsathsath");
        }
    }

    void show1EZst(int courseId)
    {
        ArrayList<EZStds> ezStdss = new ArrayList<>();
        boolean isStudent = false, isCourse = false;
        int indexOfCourse = 0;

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                isCourse = true;
                indexOfCourse = i;
                break;
            }
        }

        if (isCourse)
        {
            for (int i = 0; i < students.size(); i++)
            {
                if (students.get(i).gotCourses.contains(courses.get(indexOfCourse)))
                {
                    if (students.get(i).marks.get(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse))) < 10)
                    {
                        isStudent = true;
                        EZStds temp = new EZStds();
                        temp.ID = students.get(i).ID;
                        temp.mark = students.get(i).marks.get(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse)));
                        ezStdss.add(temp);
                    }
                }
            }
        }

        if (!isStudent)
        {
            System.out.println("nost");
        }

        else
        {
            ezStdss.sort((EZStds::compareTo));

            for (int i = 0; i < 3 && i < ezStdss.size(); i++)
            {
                System.out.print(ezStdss.get(i).ID + " ");
            }
        }

        if (isStudent)
            System.out.println();
    }

    void showEZStAll()
    {
        boolean isStudent = false;
        ArrayList<EZStds> ezStdss = new ArrayList<>();
        for (int i = 0; i < students.size(); i++)
        {

            if (students.get(i).average < 10)
            {
                isStudent = true;
                EZStds temp = new EZStds();
                temp.ID = students.get(i).ID;
                temp.mark = students.get(i).AVG();;
                ezStdss.add(temp);
            }
        }

        if (!isStudent)
        {
            System.out.print("nost");
        }
        else
        {
            ezStdss.sort(EZStds::compareTo);
            int counter = 0;

            for (int i = 0; i < 3 && i < ezStdss.size(); i++)
            {
                System.out.print(ezStdss.get(i).ID + " ");
            }
        }
        System.out.println();
    }

    void showRanks(int courseId)
    {
        ArrayList<EZStds> ezStdss = new ArrayList<>();
        boolean isCourse = false;
        int indexOfCourse = 0;

        for (int i = 0; i < courses.size(); i++)
        {
            if (courses.get(i).ID == courseId)
            {
                isCourse = true;
                indexOfCourse = i;
                break;
            }
        }

        if (isCourse)
        {
            for (int i = 0; i < students.size(); i++)
            {
                if (students.get(i).gotCourses.contains(courses.get(indexOfCourse)))
                {
                    EZStds temp = new EZStds();
                    temp.ID = students.get(i).ID;
                    temp.mark = students.get(i).marks.get(students.get(i).gotCourses.indexOf(courses.get(indexOfCourse)));
                    ezStdss.add(temp);
                }
            }
            ezStdss.sort(EZStds::compareTo);

            for (int i = 0; i < 3 && i < ezStdss.size(); i ++)
            {
                System.out.print(ezStdss.get(i).ID + " ");
            }

            System.out.println();

        }
    }

    void showRanksAll()
    {
        ArrayList<EZStds> ezStdss = new ArrayList<>();
        for (int i = 0; i < students.size(); i++)
        {
            EZStds temp = new EZStds();
            temp.ID = students.get(i).ID;
            temp.mark = students.get(i).AVG();
            ezStdss.add(temp);
        }

        ezStdss.sort(EZStds::compareTo);

        for (int i = 0; i < 3 && i < ezStdss.size(); i++)
        {
            System.out.print(ezStdss.get(i).ID +" ");
        }
        System.out.println();
    }

    void endOfReg()
    {
        int size = courses.size();
        ArrayList<Course> remove = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            if (courses.get(i).numOfStudents < 3 || !courses.get(i).gotLt)
            {
                for (int j = 0; j < students.size(); j++)
                {
                    if (students.get(j).gotCourses.contains(courses.get(i)))
                    {

                        students.get(j).marks.remove(students.get(j).marks.get(students.get(j).gotCourses.indexOf(courses.get(i))));
                        students.get(j).gotCourses.remove(courses.get(i));
                        students.get(j).gotUnits -= courses.get(i).units;
                    }
                }
                remove.add(courses.get(i));
            }
        }
        for (; remove.size() > 0;)
        {
            courses.remove(remove.get(0));
            remove.remove(remove.get(0));
        }

        ArrayList<Student> removest = new ArrayList<>();
        size = students.size();
        for (int i = 0; i < size; i++)
        {
            if (students.get(i).gotUnits < 12)
            {
                students.get(i).EZ();
                removest.add(students.get(i));
            }
        }
        for (; removest.size() > 0;)
        {
            students.remove(removest.get(0));
            removest.remove(removest.get(0));
        }

    }
}

class Student
{
    int ID;
    double average;
    int numberOfW;
    int gotUnits;
    ArrayList<Course> gotCourses = new ArrayList<>();
    ArrayList<Double> marks = new ArrayList<>();

    Student(int ID)
    {
        this.ID = ID;
    }

    void EZ()
    {
        for (int i = 0; i < gotCourses.size(); i++)
        {
            gotCourses.get(i).numOfStudents--;
        }
    }

    double AVG()
    {
        double sum = 0.;
        int size = 0;
        for (int i = 0; i < gotCourses.size(); i++)
        {
            sum += marks.get(i) * gotCourses.get(i).units;
            size += gotCourses.get(i).units;
        }
        average = sum/size;
        return sum/size;
    }
}

class Teacher
{
    int ID;
    ArrayList<Course> teachingCourses = new ArrayList<>();

    Teacher(int ID)
    {
        this.ID = ID;
    }
}

class Course
{
    int ID;
    boolean gotLt;
    int capacity = 15;
    int units;
    int numOfStudents;
    Course(int ID, int units)
    {
        this.ID = ID;
        this.units = units;
    }
}

public class Main {

    public  static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\\\d+)?");
    }

    public  static boolean isNumericDouble(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        EducationSystem edu = new EducationSystem();
        boolean startSem = false, endSem = false, endReg = false, endShow = false;
        String[] order = new String[10000];
        String temporary = new String();
        int i = 0;

        while (!startSem)
        {
            temporary = scanner.nextLine();

            for (String S: temporary.split(" "))
            {
                order[i] = new String();
                order[i] = S;
                i++;
            }
            if (i == 2 && order[0].equals("addStudent") && isNumeric(order[1]))
            {
                edu.addStudent(Integer.parseInt(order[1]));
            }

            else if (i == 2 && order[0].equals("EZ") && isNumeric(order[1]))
            {
                edu.EZ(Integer.parseInt(order[1]));
            }
            else if (i == 3 && order[0].equals("addCourse") && isNumeric(order[1]) && isNumeric(order[2]))
            {
                edu.addCourse(Integer.parseInt(order[1]), Integer.parseInt(order[2]));
            }
            else if (i >= 2 && order[0].equals(("addLecturer")) && isNumeric(order[1]))
            {
                if (i == 2)
                {
                    edu.addLecturer(Integer.parseInt(order[1]));
                }
                else
                {
                    int[] temp = new int[i - 2];
                    for (int j = 2; j < i; j++)
                    {
                        temp[j -2] = Integer.parseInt(order[j]);
                    }
                    edu.addLecturer(Integer.parseInt(order[1]), temp);
                }
            }

            else if (i == 2 && order[0].equals("EZ") && isNumeric(order[1]))
            {
                edu.EZ(Integer.parseInt(order[1]));
            }

            else if (i == 1 && order[0].equals("startSem"))
            {
                startSem = true;
            }
        i = 0;
        }

        while (startSem && !endReg)
        {
            temporary = scanner.nextLine();

            for (String S: temporary.split(" "))
            {
                order[i] = new String();
                order[i] = S;
                i++;
            }

            if (i >= 3 && order[1].equals("reg") && isNumeric(order[0]))
            {
                for (int j = 2; j < i; j ++)
                {
                    if (isNumeric(order[j])) {
                        edu.reg(Integer.parseInt(order[0]), Integer.parseInt(order[j]));
                    }
                }
            }

            else if (i == 2 && order[0].equals("EZ") && isNumeric(order[1]))
            {
                edu.EZ(Integer.parseInt(order[1]));
            }

            else if (i == 4 && order[1].equals("cap") && isNumeric(order[0]) && isNumeric(order[2]) && isNumeric(order[3]))
            {
                edu.addCap(Integer.parseInt(order[0]), Integer.parseInt(order[2]), Integer.parseInt(order[3]));
            }

            else if (i == 3 && order[0].equals("W") && isNumeric(order[1]) && isNumeric(order[2]))
            {
                edu.W(Integer.parseInt(order[1]), Integer.parseInt(order[2]));
            }

            else if (i == 1 && order[0].equals("endReg"))
            {
                edu.endOfReg();
                endReg = true;
            }
            i = 0;
        }

        while (startSem && !endSem)
        {
            temporary = scanner.nextLine();

            for (String S: temporary.split(" "))
            {
                order[i] = new String();
                order[i] = S;
                i++;
            }

            if (i == 3 && order[0].equals("W") && isNumeric(order[1]) && isNumeric(order[2]))
            {
                edu.W(Integer.parseInt(order[1]), Integer.parseInt(order[2]));
            }
            else if (i == 2 && order[0].equals("EZ") && isNumeric(order[1]))
            {
                edu.EZ(Integer.parseInt(order[1]));
            }
            else if (i == 5 && order[1].equals("mark") && isNumeric(order[0]) && isNumeric(order[2]) && isNumericDouble(order[3]) && order[4].equals("-all"))
            {
                edu.markAll(Integer.parseInt(order[0]), Integer.parseInt(order[2]),Double.parseDouble(order[3]));
            }
            else if (i == 4 && order[1].equals("pureOghde") && isNumeric(order[0]) && isNumeric(order[2]) && isNumeric(order[3]))
            {
                edu.pureOghde1_1(Integer.parseInt(order[0]), Integer.parseInt(order[3]), Integer.parseInt(order[2]));
            }
            else if (i == 4 && order[1].equals("pureOghde") && isNumeric(order[0]) && isNumeric(order[2]) && order[3].equals("-all"))
            {
                edu.pureOghde1_all(Integer.parseInt(order[0]), Integer.parseInt(order[2]));
            }
            else if (i == 3 && order[1].equals("pureOghde") && order[2].equals("-all") && isNumeric(order[0]))
            {
                edu.pureOghdeAll_all(Integer.parseInt(order[0]));
            }
            else if (i >= 5 && order[1].equals("mark") && isNumeric(order[0]))
            {
                for (int j = 3; j < i - 1; j += 2)
                {
                    if (isNumericDouble(order[j+1])) {
                        edu.markTaki(Integer.parseInt(order[0]), Integer.parseInt(order[2]), Integer.parseInt(order[j]), Double.parseDouble(order[j+1]));
                    }
                }
            }

            else if (i == 1 && order[0].equals("endSem"))
            {
                endSem = true;
            }
            i = 0;
        }

        while (startSem && !endShow)
        {
            temporary = scanner.nextLine();

            for (String S: temporary.split(" "))
            {
                order[i] = new String();
                order[i] = S;
                i++;
            }

            if (i == 3 && order[0].equals("showCourse"))
            {
                edu.showCourse(Integer.parseInt(order[1]), order[2]);
            }

            else if (i == 2 && order[0].equals("showAvg"))
            {
                edu.showAvg(Integer.parseInt(order[1]));
            }

            else if (i == 2 && order[0].equals("showEZSt") && isNumeric(order[1]))
            {

                edu.show1EZst(Integer.parseInt(order[1]));
            }

            else if (i == 2 && order[0].equals("showEZSt") && order[1].equals("-all"))
            {
                edu.showEZStAll();
            }

            else if (i == 2 && order[0].equals("showRanks") && isNumeric(order[1]))
            {
                edu.showRanks(Integer.parseInt(order[1]));
            }

            else if (i == 2 && order[0].equals("showRanks") && order[1].equals("-all"))
            {
                edu.showRanksAll();
            }

            else if (i == 1 && order[0].equals("endShow"))
            {
                endShow = true;
            }
            i = 0;
        }
    }
}
