![Quap](.\Client\DesktopApp\src\main\resources\com\quap\images\splashBackground.jpg)

CONTENTS OF THIS FILE
---------------------

* Introduction
* Requirements
* Recommended modules
* Installation
* Configuration
* Troubleshooting
* FAQ
* Maintainers

INTRODUCTION
------------

The Administration Menu module displays the entire administrative menu tree
(and most local tasks) in a drop-down menu, providing administrators one- or
two-click access to most pages.  Other modules may also add menu links to the
menu using hook_admin_menu_output_alter().

* For a full description of the module, visit the project page:
  https://www.drupal.org/project/admin_menu

* To submit bug reports and feature suggestions, or track changes:
  https://www.drupal.org/project/issues/admin_menu

REQUIREMENTS
------------

This module requires the following modules:

* [Views](https://www.drupal.org/project/views)
* [Panels](https://www.drupal.org/project/panels)

REQUIREMENTS
------------

This module requires no modules outside of Drupal core.

RECOMMENDED MODULES
-------------------

* [Markdown filter](https://www.drupal.org/project/markdown):
  When enabled, display of the project's README.md help will be rendered
  with markdown.

INSTALLATION
------------

* Install as you would normally install a contributed Drupal module. Visit
  https://www.drupal.org/node/895232/ for further information.

* You may want to disable Toolbar module, since its output clashes with
  Administration Menu.

INSTALLATION
------------

* Install as you would normally install a contributed Drupal module. Visit
  https://www.drupal.org/node/1897420 for further information.

CONFIGURATION
-------------

* Configure the user permissions in Administration » People » Permissions:

    - Use the administration pages and help (System module)

      The top-level administration categories require this permission to be
      accessible. The administration menu will be empty unless this permission
      is granted.

    - Access administration menu

      Users with this permission will see the administration menu at the top of
      each page.

    - Display Drupal links

      Users with this permission will receive links to drupal.org issue queues
      for all enabled contributed modules. The issue queue links appear under
      the administration menu icon.

* Customize the menu settings in Administration » Configuration and modules »
  Administration » Administration menu.

* To prevent administrative menu items from appearing twice, you may hide the
  "Management" menu block.

CONFIGURATION
-------------

The module has no menu or modifiable settings. There is no configuration. When
enabled, the module will prevent the links from appearing. To get the links
back, disable the module and clear caches.

TROUBLESHOOTING
---------------

* If the menu does not display, check the following:

    - Are the "Access administration menu" and "Use the administration pages
      and help" permissions enabled for the appropriate roles?

    - Does html.tpl.php of your theme output the $page_bottom variable?

FAQ
---

Q: I enabled "Aggregate and compress CSS files", but admin_menu.css is still
there. Is this normal?

A: Yes, this is the intended behavior. the administration menu module only loads
its stylesheet as needed (i.e., on page requests by logged-on, administrative
users).

MAINTAINERS
-----------

Current maintainers:
* Daniel F. Kudwien (sun) - https://www.drupal.org/user/54136
* Peter Wolanin (pwolanin) - https://www.drupal.org/user/49851
* Stefan M. Kudwien (smk-ka) - https://www.drupal.org/user/48898
* Dave Reid (Dave Reid) - https://www.drupal.org/user/53892

This project has been sponsored by:
* UNLEASHED MIND
  Specialized in consulting and planning of Drupal powered sites, UNLEASHED
  MIND offers installation, development, theming, customization, and hosting
  to get you started. Visit https://www.unleashedmind.com for more information.
