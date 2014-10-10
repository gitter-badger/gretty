/*
 * Gretty
 *
 * Copyright (C) 2013-2014 Andrey Hihlovskiy and contributors.
 *
 * See the file "LICENSE" for copying and usage permission.
 * See the file "CONTRIBUTORS" for complete list of contributors.
 */
package org.akhikhl.gretty

import org.gradle.api.Project

/**
 *
 * @author akhikhl
 */
class GradleUtils {
  
  static boolean derivedFrom(Class targetClass, String className) {
    while(targetClass != null) {
      if(targetClass.getName() == className)
        return true
      for(Class intf in targetClass.getInterfaces())
        if(derivedFrom(intf, className))
          return true
      targetClass = targetClass.getSuperclass()
    }
    return false
  }
  
  /**
   * Replacement for instanceof operator, workaround for Gradle 1.10 bug:
   * task classes defined in "build.gradle" fail instanceof check for base classes in gradle plugins.
   */
  static boolean instanceOf(Object obj, String className) {
    derivedFrom(obj.getClass(), className)
  }

  static void disableTaskOnOtherProjects(Project thisProject, String taskName) {
    thisProject.rootProject.allprojects { proj ->
      if(proj != thisProject && proj.tasks.findByName(taskName))
        proj.tasks[taskName].enabled = false
    }
  }

  private static final boolean IS_INCREMENTAL_TASK_SUPPORTED
  static {
    try {
      Class<?> clazz = Class.forName('org.gradle.api.tasks.incremental.IncrementalTaskInputs')
      IS_INCREMENTAL_TASK_SUPPORTED = true
    } catch (LinkageError | ClassNotFoundException cnfe) {
      IS_INCREMENTAL_TASK_SUPPORTED = false
    }
  }
  static boolean isIncrementalTaskSupported() {
    return IS_INCREMENTAL_TASK_SUPPORTED
  }
}

