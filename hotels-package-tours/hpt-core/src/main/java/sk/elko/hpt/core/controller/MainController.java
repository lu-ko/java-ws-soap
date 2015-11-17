package sk.elko.hpt.core.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sk.elko.hpt.core.controller.form.AboutForm;
import sk.elko.hpt.core.controller.form.DbStateForm;
import sk.elko.hpt.core.service.AppVersionService;
import sk.elko.hpt.core.service.DestinationService;
import sk.elko.hpt.core.service.HotelService;
import sk.elko.hpt.core.service.PackageService;

/**
 * Simple controller to render home/welcome page with count of entities in DB.
 */
@Controller
public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping(value = { "/ui" }, method = RequestMethod.GET)
    public String renderHome(Model model) {
        log.info("renderHome - Rendering home/welcome page ...");

        DbStateForm dbStateForm = new DbStateForm();
        dbStateForm.setDestinations(destinationService.findAll().size());
        dbStateForm.setHotels(hotelService.findAll().size());
        dbStateForm.setPackages(packageService.findAll().size());
        dbStateForm.setDatetime(new DateTime());
        model.addAttribute("dbState", dbStateForm);

        AboutForm aboutForm = new AboutForm();
        aboutForm.setBuildTime(appVersionService.getBuildTime());
        aboutForm.setHptCoreVersion(appVersionService.getHptCoreVersion());
        aboutForm.setHptSchemaVersion(appVersionService.getHptSchemaVersion());
        model.addAttribute("about", aboutForm);

        return "home";
    }

    @RequestMapping(value = { "/ui" }, method = RequestMethod.POST, params = { "refresh" })
    public String refreshHome(Model model) {
        log.info("refreshHome - Request to refresh DB state ...");

        return "redirect:/ui";
    }

}
